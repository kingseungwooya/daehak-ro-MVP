package daehakro.server.domain.admin.controller;

import static daehakro.server.domain.admin.controller.AdminController.REST_URL_ADMIN;

import daehakro.server.domain.admin.controller.dto.request.EventDto;
import daehakro.server.domain.admin.controller.dto.request.MatchDto;
import daehakro.server.domain.admin.controller.dto.response.EventMatchDto;
import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.admin.service.AdminService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(REST_URL_ADMIN)
public class AdminController {

    public static final String REST_URL_ADMIN = "api/mvp/admin";
    private final AdminService adminService;

    @PostMapping("/event/match")
    public ResponseEntity<?> matching(@RequestBody MatchDto matchDto) {
        adminService.randomMatch(matchDto.getEventId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/event/close")
    public ResponseEntity<?> closeEvent(@RequestParam Long eventId) {
        adminService.closeEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/event")
    public ResponseEntity<EventResDto> createEvent(@RequestBody EventDto eventDto) {
        return new ResponseEntity<>(adminService.createEvent(eventDto), HttpStatus.CREATED);
    }

    @GetMapping("/couples/{eventId}")
    public ResponseEntity<EventMatchDto> getMatchedCouple(@PathVariable Long eventId) {
        return new ResponseEntity<>(adminService.getMatchedCouples(eventId), HttpStatus.OK);
    }

    @GetMapping("/team-couples/{eventId}")
    public ResponseEntity<?> getMatchedTeamCouple(@PathVariable Long eventId) {
        return new ResponseEntity<>(adminService.getMatchedTeam(eventId), HttpStatus.OK);
    }
}
