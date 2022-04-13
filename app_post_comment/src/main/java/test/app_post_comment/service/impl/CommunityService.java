package test.app_post_comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.app_post_comment.domain.Community;
import test.app_post_comment.dto.CommunityDto;
import test.app_post_comment.exception.CommunityNotFoundException;
import test.app_post_comment.mapper.CommunityMapper;
import test.app_post_comment.repository.CommunityRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommunityService {
    private final CommunityRepository repository;
    private final CommunityMapper mapper;

    public CommunityDto create(CommunityDto dto) {
        Community savedCommunity = repository.save(mapper.fromDto(dto));
        dto.setId(savedCommunity.getId());
        return dto;
    }

    public List<CommunityDto> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public CommunityDto findById(Integer id) {
        Community community = repository.findById(id)
                .orElseThrow(() -> new CommunityNotFoundException("Community not found with ID - " + id));
        return mapper.toDto(community);
    }
}
