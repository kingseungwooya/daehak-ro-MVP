package daehakro.server.domain.event.controller;

import static daehakro.server.domain.event.controller.EventController.REST_URL_EVENT;

import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.event.controller.dto.request.MemberApplyForm;
import daehakro.server.domain.event.controller.dto.request.TeamApplyForm;
import daehakro.server.domain.event.service.EventService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.
        web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(REST_URL_EVENT)
public class EventController {

    public static final String REST_URL_EVENT = "api/mvp/event";

    private final EventService eventService;
    /**
     * 개인 참여 신청
     */
    @PostMapping("/apply-individual")
    public ResponseEntity applyIndividual(@RequestBody MemberApplyForm applyForm) {
        eventService.applyEvent(applyForm);
        return ResponseEntity.ok().build();
    }
    /**
     * 팀 참여 신청
     */
    @PostMapping("/apply-team")
    public ResponseEntity applyTeam(@RequestBody TeamApplyForm applyForm) {
        eventService.applyTeamEvent(applyForm);
        return ResponseEntity.ok().build();
    }


    @GetMapping("")
    public ResponseEntity<List<EventResDto>> eventInfo() {
        return new ResponseEntity<>(eventService.getAllEvents(), HttpStatus.OK);
    }

    @GetMapping("/couple")
    public ResponseEntity<?> couple() {
        return null;
    }
}
