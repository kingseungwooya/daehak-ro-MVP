package daehakro.server.domain.admin.service;


import daehakro.server.domain.admin.controller.dto.request.EventDto;
import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {

    void randomMatch(Long eventId);

    void randomTeamMatch(Long eventId);

    void createEvent(EventDto eventDto);

    void closeEvent(Long eventId);

    void closeTeamEvent(Long eventId);

}
