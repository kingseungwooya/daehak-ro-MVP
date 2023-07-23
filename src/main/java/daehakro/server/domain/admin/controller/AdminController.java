package daehakro.server.domain.admin.controller;

import static daehakro.server.domain.admin.controller.AdminController.REST_URL_ADMIN;
import static daehakro.server.domain.event.controller.EventController.REST_URL_EVENT;

import daehakro.server.domain.admin.controller.dto.request.EventDto;
import daehakro.server.domain.admin.controller.dto.response.EventResDto;
import daehakro.server.domain.admin.controller.dto.response.UnivInfoDto;
import daehakro.server.domain.admin.service.AdminService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PostMapping("/matching")
    public ResponseEntity<?> matching(@RequestParam Long eventId) {
        adminService.randomMatch(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/team-matching")
    public ResponseEntity<?> teamMatching(@RequestParam Long eventId) {
        adminService.randomTeamMatch(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/event/close")
    public ResponseEntity<?> closeEvent(@RequestParam Long eventId) {
        adminService.closeEvent(eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto) {
        adminService.createEvent(eventDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
