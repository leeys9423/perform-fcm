package com.example.fcm.domain.attendance;

import com.example.fcm.config.TestConfig;
import com.example.fcm.domain.attendance.dto.request.AttendanceRequest;
import com.example.fcm.domain.attendance.service.AttendanceService;
import com.example.fcm.domain.member.entity.Member;
import com.example.fcm.domain.member.repository.MemberRepository;
import com.example.fcm.domain.studentParent.entity.StudentParent;
import com.example.fcm.domain.studentParent.repository.StudentParentRepository;
import com.example.fcm.global.common.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test") // test 프로필 활성화
@Import(TestConfig.class) // 테스트 설정 가져오기
@Transactional // 테스트 후 데이터 롤백
public class AttendanceServiceBasicTest {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StudentParentRepository studentParentRepository;

    // 테스트용 회원 ID
    private Long studentId;

    @BeforeEach
    public void setUp() {
        // 1. 테스트용 학생(회원) 생성
        Member student = Member.builder()
                .name("테스트 학생")
                .status(Status.ACTIVE)
                .build();
        Member savedStudent = memberRepository.save(student);
        this.studentId = savedStudent.getId();

        // 2. 학생의 부모 생성
        StudentParent parent = StudentParent.builder()
                .studentId(this.studentId)
                .name("테스트 부모")
                .status(Status.ACTIVE)
                .build();
        studentParentRepository.save(parent);
    }

    @DisplayName("출석체크 기본 테스트")
    @Test
    public void attendance_basic_test() {
        // 1. 출석 요청 준비
        AttendanceRequest request = new AttendanceRequest(this.studentId);

        // 2. 출석 체크 실행
        var response = attendanceService.checkAttendance(request);

        // 3. 결과 확인
        assertNotNull(response, "출석 응답이 null이 아니어야 합니다.");
        System.out.println("출석 체크 완료: " + response.getStudentName() + ", 시간: " + response.getCheckTime());
    }
}
