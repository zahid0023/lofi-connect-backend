package org.example.loficonnect.dto.mapper.custom.fields;

import lombok.Data;
import org.example.loficonnect.dto.request.custom.fields.CustomFieldCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class GoHighLevelCustomFieldCreateRequest {
    private String name;
    private String dataType;
    private String placeholder;
    private List<String> acceptedFormat;
    private Boolean isMultipleFile;
    private Integer maxNumberOfFiles;
    private List<TextBoxListOption> textBoxListOptions;
    private Integer position;
    private String model;

    @Data
    public static class TextBoxListOption {
        private String label;
        private String prefillValue;
    }

    public static GoHighLevelCustomFieldCreateRequest fromRequest(CustomFieldCreateRequest request) {
        GoHighLevelCustomFieldCreateRequest ghlRequest = new GoHighLevelCustomFieldCreateRequest();

        ghlRequest.setName(request.getName());
        ghlRequest.setDataType(request.getDataType());
        ghlRequest.setPlaceholder(request.getPlaceholder());
        ghlRequest.setAcceptedFormat(request.getAcceptedFormat());
        ghlRequest.setIsMultipleFile(request.getIsMultipleFile());
        ghlRequest.setMaxNumberOfFiles(request.getMaxNumberOfFiles());
        ghlRequest.setPosition(request.getPosition());
        ghlRequest.setModel(request.getModel());

        if (request.getTextBoxListOptions() != null) {
            ghlRequest.setTextBoxListOptions(
                request.getTextBoxListOptions().stream().map(opt -> {
                    TextBoxListOption option = new TextBoxListOption();
                    option.setLabel(opt.getLabel());
                    option.setPrefillValue(opt.getPrefillValue());
                    return option;
                }).collect(Collectors.toList())
            );
        }

        return ghlRequest;
    }
}
