package com.alex.games.library.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alex.games.library.model.ERole;
import com.alex.games.library.model.Role;
import com.alex.games.library.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	public List<String> fromSetToStringList(Set<Role> roles) {
		return roles.stream().map(role -> role.getName().toString()).collect(Collectors.toList());
	}

	public Optional<Role> getByName(ERole roleUser) {
		return roleRepository.findByName(roleUser);
	}
	
	public Optional<Role> getById(Long id){
		return roleRepository.findById(id);
	}
	public List<Role> getAll() {
		return roleRepository.findAll();
	}
}
