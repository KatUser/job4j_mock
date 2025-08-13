package ru.checkdev.mock.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.checkdev.mock.domain.Submitter;
import ru.checkdev.mock.dto.SubmitterDTO;


@Component
@AllArgsConstructor
public class SubmitterMapper {

    public Submitter getSubmitter(SubmitterDTO submitterDTO) {
        return Submitter.of()
                .id(submitterDTO.getId())
                .name(submitterDTO.getName())
                .build();
    }

}
