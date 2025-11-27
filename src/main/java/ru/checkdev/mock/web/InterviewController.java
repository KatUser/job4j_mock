package ru.checkdev.mock.web;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.exception.ItemNotFoundException;
import ru.checkdev.mock.service.InterviewService;

import javax.validation.Valid;
import java.sql.SQLException;

@RestController
@RequestMapping("/interview")
@AllArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @Getter
    private final Counter errorCounter;

    @Autowired
    public InterviewController(InterviewService interviewService, MeterRegistry meterRegistry) {
        this.interviewService = interviewService;
        this.errorCounter = meterRegistry.counter("errors");
    }


    @PostMapping("/")
    public ResponseEntity<Interview> save(@Valid @RequestBody Interview interview) throws SQLException {
        return new ResponseEntity<>(
                interviewService
                        .save(interview)
                        .orElseThrow(() -> new SQLException("An error occurred while saving data")),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interview> getById(@Valid @PathVariable int id) {
        var foundInterviews= interviewService.findById(id);
        if (foundInterviews.isEmpty()) {
            errorCounter.increment();
            throw new ItemNotFoundException("Item not found");
        }
        return ResponseEntity.ok(foundInterviews.get());
    }


    @PutMapping("/")
    public ResponseEntity<Interview> update(@Valid @RequestBody Interview interview) {
        return new ResponseEntity<>(interview,
                interviewService.update(interview) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/status/")
    public ResponseEntity<HttpStatus> updateStatusInterview(@RequestParam String interviewId,
                                                            @RequestParam String interviewNewStatus) {
        var id = Integer.parseInt(interviewId);
        var statusId = Integer.parseInt(interviewNewStatus);

        if (interviewService.findById(id).isEmpty()
                || interviewService.findStatusById(statusId).isEmpty()) {
            errorCounter.increment();
            throw new ItemNotFoundException("Item not found");
        }

        var result = interviewService.updateStatus(id, statusId);
        return ResponseEntity.status(result ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Interview> delete(@Valid @PathVariable int id) {
        if (interviewService.findById(id).isEmpty()) {
            errorCounter.increment();
            throw new ItemNotFoundException("Item not found");
        }
        Interview interview = new Interview();
        interview.setId(id);
        return new ResponseEntity<>(interview,
                interviewService.delete(interview) ? HttpStatus.OK : HttpStatus.NO_CONTENT);
    }

}
