package ru.practicum.main.user.service.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.user.dto.CreateUserDto;
import ru.practicum.main.user.dto.UserResponseDto;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.AdminUserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.user.mapper.UserMapper.userRequestDtoToUser;
import static ru.practicum.main.user.mapper.UserMapper.userToUserResponseDto;

@Service
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserRepository adminUserRepository;

    public AdminUserServiceImpl(AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public List<UserResponseDto> getUsers(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return adminUserRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(UserMapper::userToUserResponseDto)
                    .collect(Collectors.toList());
        }

        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<UserResponseDto> users = adminUserRepository.findByIdIn(ids, pageRequest)
                .stream()
                .map(UserMapper::userToUserResponseDto)
                .collect(Collectors.toList());

        log.info("Found users: {}", users);

        return users;
    }

    @Override
    public UserResponseDto createUser(CreateUserDto dto) {
        if (adminUserRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("User with email: " + dto.getEmail() + " already exists");
        }

        User user = adminUserRepository.save(userRequestDtoToUser(dto));
        UserResponseDto responseDto = userToUserResponseDto(user);
        responseDto.setId(user.getId());

        log.info("Created user: {} with id: {}", user.getName(), user.getId());

        return responseDto;
    }

    @Override
    public String deleteUser(Long userId) {
        if (adminUserRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id: " + userId + " doesn't exist");
        }

        adminUserRepository.deleteById(userId);

        log.info("Deleted user with id: {}", userId);
        return "User with id: " + userId + " was deleted";
    }
}
