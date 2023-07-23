package daehakro.server.domain.event.service;

import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.event.controller.dto.request.MemberApplyForm;
import daehakro.server.domain.event.controller.dto.request.TeamApplyForm;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface EventService {

    // 이벤트 신청하기
    void applyEvent(MemberApplyForm applyForm);

    void applyTeamEvent(TeamApplyForm applyForm);

    List<EventResDto> getAllEvents();
}
