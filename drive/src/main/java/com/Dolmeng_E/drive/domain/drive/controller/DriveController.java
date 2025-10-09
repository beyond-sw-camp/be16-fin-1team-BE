package com.Dolmeng_E.drive.domain.drive.controller;

import com.Dolmeng_E.drive.domain.drive.dto.FolderSaveDto;
import com.Dolmeng_E.drive.domain.drive.dto.FolderUpdateNameDto;
import com.Dolmeng_E.drive.domain.drive.service.DriverService;
import com.example.modulecommon.dto.CommonSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drive")
@RequiredArgsConstructor
public class DriveController {

    private final DriverService driverService;

    // 폴더 생성
    @PostMapping("/folder")
    public ResponseEntity<?> saveFolder(@RequestBody FolderSaveDto folderSaveDto) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(driverService.createFolder(folderSaveDto))
                .statusCode(HttpStatus.CREATED.value())
                .statusMessage("폴더 생성 성공")
                .build(), HttpStatus.CREATED);
    }

    // 폴더명 수정
    @PutMapping("/folder/{folder_id}")
    public ResponseEntity<?> updateFolder(@RequestBody FolderUpdateNameDto folderUpdateNameDto, @PathVariable String folder_id) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(driverService.updateFolderName(folderUpdateNameDto, folder_id))
                .statusCode(HttpStatus.OK.value())
                .statusMessage("폴더명 수정 성공")
                .build(), HttpStatus.OK);
    }

    // 폴더 삭제
    @DeleteMapping("/folder/{folder_id}")
    public ResponseEntity<?> deleteFolder(@PathVariable String folder_id) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(driverService.deleteFolder(folder_id))
                .statusCode(HttpStatus.OK.value())
                .statusMessage("폴더 삭제 성공")
                .build(), HttpStatus.OK);
    }

    // 폴더 하위 요소들 조회
    @GetMapping("/folder/{folder_id}/contents")
    public ResponseEntity<?> getFolderContents(@PathVariable String folder_id) {
        return new ResponseEntity<>(CommonSuccessDto.builder()
                .result(driverService.getFolderContents(folder_id))
                .statusCode(HttpStatus.OK.value())
                .statusMessage("폴더 하위 요소들 조회 성공")
                .build(), HttpStatus.OK);
    }
}
