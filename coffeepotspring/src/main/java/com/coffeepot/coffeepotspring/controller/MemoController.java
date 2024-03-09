package com.coffeepot.coffeepotspring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coffeepot.coffeepotspring.dto.MemoRequestDTO;
import com.coffeepot.coffeepotspring.dto.MemoResponseDTO;
import com.coffeepot.coffeepotspring.dto.ResponseDTO;
import com.coffeepot.coffeepotspring.service.MemoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {
	
	private final MemoService memoService;
	
	@PostMapping
	public ResponseEntity<?> createMemo(@AuthenticationPrincipal String userId, @ModelAttribute MemoRequestDTO memoRequestDTO) {
		try {
			List<MemoResponseDTO> responseDTOs = List.of(memoService.create(userId, memoRequestDTO));
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().data(responseDTOs).build();
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> retrieveAllMemoList(
			@RequestParam(defaultValue = "") String memoId,
			@RequestParam(defaultValue = "5") int pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy) {
		try {
			List<MemoResponseDTO> responseDTOs = memoService.retrieve(memoId, pageSize, sortBy);
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().data(responseDTOs).build();
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/my-memo-page")
	public ResponseEntity<?> retrieveMyMemoList(
			@AuthenticationPrincipal String userId,
			@RequestParam(defaultValue = "") String memoId,
			@RequestParam(defaultValue = "5") int pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy) {
		try {
			List<MemoResponseDTO> responseDTOs = memoService.retrieveMyMemoPage(userId, memoId, pageSize, sortBy);
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().data(responseDTOs).build();
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PutMapping
	public ResponseEntity<?> updateMemo(@AuthenticationPrincipal String userId, @ModelAttribute MemoRequestDTO memoRequestDTO) {
		try {
			List<MemoResponseDTO> responseDTOs = List.of(memoService.update(userId, memoRequestDTO)); 
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().data(responseDTOs).build();
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/{memoId}")
	public ResponseEntity<?> deleteMemo(
			@AuthenticationPrincipal String userId, @PathVariable String memoId) {
		try {
			memoService.delete(userId, memoId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PostMapping("/like")
	public ResponseEntity<?> likeMemo(@AuthenticationPrincipal String userId, @RequestBody Map<String, String> memoId) {
		try {
			memoService.like(userId, memoId.get("memoId"));
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/like/{memoId}")
	public ResponseEntity<?> unlikeMemo(@AuthenticationPrincipal String userId, @PathVariable String memoId) {
		try {
			memoService.unlike(userId, memoId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PostMapping("/scrap")
	public ResponseEntity<?> scrapMemo(@AuthenticationPrincipal String userId, @RequestBody Map<String, String> memoId) {
		try {
			memoService.scrap(userId, memoId.get("memoId"));
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@DeleteMapping("/scrap/{memoId}")
	public ResponseEntity<?> unscrapMemo(@AuthenticationPrincipal String userId, @PathVariable String memoId) {
		try {
			memoService.unscrap(userId, memoId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (Exception e) {
			String error = e.getMessage();
			ResponseDTO<MemoResponseDTO> response = ResponseDTO.<MemoResponseDTO>builder().error(error).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

}
