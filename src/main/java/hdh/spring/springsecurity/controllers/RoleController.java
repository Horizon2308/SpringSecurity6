package hdh.spring.springsecurity.controllers;

import hdh.spring.springsecurity.http_responses.ErrorResponse;
import hdh.spring.springsecurity.http_responses.SuccessResponse;
import hdh.spring.springsecurity.models.dtos.RoleDTO;
import hdh.spring.springsecurity.services.IRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/admin/role")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping("")
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO,
                                        BindingResult result) {
        if (result.hasErrors()) {
            List<String> messages = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(new ErrorResponse<>(messages));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(roleService.createRole(roleDTO),
                "Created role successfully!"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.ok(new SuccessResponse<>("Deleted"));
    }

}
