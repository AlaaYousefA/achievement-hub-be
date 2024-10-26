package com.achivhub.backend.application.controllers;

import com.achivhub.backend.domain.model.SysUser;
import com.achivhub.backend.domain.services.SysUsersService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class SysUsersController {
    private final SysUsersService sysUsersService;


    @GetMapping("/{id}")
    @RolesAllowed({"ADMIN", "SUB_ADMIN"})
    public ResponseEntity<SysUser> getSysUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(sysUsersService.getSysUser(id));
    }

    @GetMapping
    @RolesAllowed({"ADMIN", "SUB_ADMIN"})
    public ResponseEntity<Page<SysUser>> getAllSysUsers(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "createdAt", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "sortDirection") String sortDirection,
            @RequestParam(name = "department", required = false) List<String> department,
            @RequestParam(name = "kasitRole", required = false) List<String> kasitRole,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "achNo", required = false) Long achievementsNumber
    ) {
        return ResponseEntity.ok(sysUsersService.getAllSysUsers(page, size, sortBy, sortDirection, department, kasitRole, name, achievementsNumber));
    }

    @PutMapping("/upgrade-idm")
    @RolesAllowed("ADMIN")
    public ResponseEntity<Boolean> upgrade(@RequestParam("username") String username, @RequestParam("roles") String roles) {
        return ResponseEntity.ok(
                sysUsersService.upgradeIdmRoles(username, roles)
        );
    }

}
