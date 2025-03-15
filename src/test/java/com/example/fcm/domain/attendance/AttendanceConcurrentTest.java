package com.example.fcm.domain.attendance;

import com.example.fcm.config.TestConfig;
import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.entity.Attendance;
import com.example.fcm.domain.attendance.repository.AttendanceRepository;
import com.example.fcm.domain.attendance.service.AttendanceService;
import com.example.fcm.domain.device.entity.Device;
import com.example.fcm.domain.device.repository.DeviceRepository;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.repository.MemberRepository;
import com.example.fcm.domain.notification.dto.message.PushMessageEvent;
import com.example.fcm.domain.notification.repository.PushHistoryRepository;
import com.example.fcm.domain.notification.service.PushMessageService;
import com.example.fcm.domain.studentParent.dto.response.StudentParentFcmResponse;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.facade.StudentParentFacade;
import com.example.fcm.domain.studentParent.repository.StudentParentRepository;
import com.example.fcm.global.common.Status;
import com.example.fcm.infra.redis.PushMessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class AttendanceConcurrentTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudentParentRepository studentParentRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @MockitoBean
    private PushMessageService pushMessageService;

    // 테스트용 학생 목록
    private List<Long> studentIds = new ArrayList<>();

    // 설정값
    private static final int STUDENT_COUNT = 20; // 학생 수
    private static final int PARENTS_PER_STUDENT = 2; // 학생 당 부모 수
    private static final int CONCURRENT_USERS = 200; // 동시 사용자 수

    @BeforeEach
    @Transactional
    public void setUp() {
        // 모킹 설정
        doNothing().when(pushMessageService).sendPushMessage(any(PushMessageEvent.class));

        // 테스트 데이터 생성
        createTestData();

        // 데이터 검증
        System.out.println("테스트 데이터 생성 요약:");
        System.out.println("- 학생 수: " + studentIds.size());
        System.out.println("- 부모 수: " + studentParentRepository.count());
        System.out.println("- 디바이스 수: " + deviceRepository.count());
    }

    /**
     * 테스트 데이터 생성
     * - 20명의 학생
     * - 각 학생당 1명의 부모
     * - 각 부모당 1개의 디바이스
     */
    private void createTestData() {
        System.out.println("테스트 데이터 생성 시작...");

        // 기존 데이터 정리
        studentIds.clear();

        // 학생, 부모, 디바이스 생성
        for (int i = 0; i < STUDENT_COUNT; i++) {
            // 1. 학생 생성
            Member student = Member.builder()
                    .name("완전 테스트 학생 " + i)
                    .status(Status.ACTIVE)
                    .build();
            Member savedStudent = memberRepository.save(student);
            this.studentIds.add(savedStudent.getId());

            // 2. 각 학생당 부모 생성
            for (int j = 0; j < PARENTS_PER_STUDENT; j++) {
                StudentParent parent = StudentParent.builder()
                        .studentId(savedStudent.getId())
                        .name("완전 테스트 부모 " + i + "-" + j)
                        .status(Status.ACTIVE)
                        .build();
                StudentParent savedParent = studentParentRepository.save(parent);

                // 3. 각 부모당 디바이스 생성 (FCM 토큰 포함)
                Device device = Device.builder()
                        .parentId(savedParent.getId())
                        .fcmToken("test-fcm-token-" + UUID.randomUUID().toString())
                        .status(Status.ACTIVE)
                        .build();
                deviceRepository.save(device);
            }
        }

        System.out.println("테스트 데이터 생성 완료: " +
                STUDENT_COUNT + "명 학생, " +
                (STUDENT_COUNT * PARENTS_PER_STUDENT) + "명 부모, " +
                (STUDENT_COUNT * PARENTS_PER_STUDENT) + "개 디바이스 생성됨");
    }

    @DisplayName("출석체크 동시성 테스트 200명")
    @Test
    public void attendance_concurrent_test_200() throws InterruptedException {
        // 1. 동시성 테스트를 위한 설정
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        CountDownLatch latch = new CountDownLatch(CONCURRENT_USERS);
        AtomicInteger successCounter = new AtomicInteger(0);
        AtomicInteger errorCounter = new AtomicInteger(0);

        // 시작 시간 기록
        long startTime = System.currentTimeMillis();

        // 2. 동시 요청 제출
        for (int i = 0; i < CONCURRENT_USERS; i++) {
            final int index = i;
            executor.submit(() -> {
                try {
                    // 학생 ID 선택 (라운드 로빈 방식)
                    Long studentId = studentIds.get(index % STUDENT_COUNT);

                    // 출석 요청 생성
                    AttendanceRequest request = new AttendanceRequest(studentId);

                    // 출석 체크 실행
                    var response = attendanceService.checkAttendance(request);

                    // 성공 카운트 증가
                    successCounter.incrementAndGet();
                } catch (Exception e) {
                    // 실패 카운트 증가
                    errorCounter.incrementAndGet();
                    e.printStackTrace();
                } finally {
                    // 작업 완료 신호
                    latch.countDown();
                }
            });
        }

        // 3. 모든 작업이 완료될 때까지 대기 (최대 60초)
        boolean completed = latch.await(60, TimeUnit.SECONDS);

        // 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // 4. 실행기 종료
        executor.shutdown();

        // 5. 결과 출력
        System.out.println("=== 테스트 결과 ===");
        System.out.println("모든 요청 완료: " + completed);
        System.out.println("성공 요청: " + successCounter.get());
        System.out.println("실패 요청: " + errorCounter.get());
        System.out.println("총 소요시간: " + totalTime + "ms");
        System.out.println("요청당 평균 시간: " + (totalTime / (double)CONCURRENT_USERS) + "ms");

        // 6. PushMessageService 검증
        // 각 학생당 부모가 PARENTS_PER_STUDENT명씩 있으므로
        // 각 요청마다 PushMessageEvent가 PARENTS_PER_STUDENT번 생성됨
        verify(pushMessageService, times(successCounter.get() * PARENTS_PER_STUDENT))
                .sendPushMessage(any(PushMessageEvent.class));

        // 7. 결과 검증
        assertTrue(completed, "모든 요청이 제한 시간 내에 완료되어야 합니다");
        assertEquals(CONCURRENT_USERS, successCounter.get(), "모든 요청이 성공해야 합니다");
        assertEquals(0, errorCounter.get(), "오류가 발생하지 않아야 합니다");
    }
}
