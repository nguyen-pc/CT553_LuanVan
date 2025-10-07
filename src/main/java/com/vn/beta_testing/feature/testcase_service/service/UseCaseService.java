package com.vn.beta_testing.feature.testcase_service.service;

import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.beta_testing.domain.Campaign;
import com.vn.beta_testing.domain.Project;
import com.vn.beta_testing.domain.UseCase;
import com.vn.beta_testing.domain.response.ResultPaginationDTO;
import com.vn.beta_testing.feature.company_service.service.CampaignService;
import com.vn.beta_testing.feature.testcase_service.repository.UseCaseRepository;
import com.vn.beta_testing.util.error.IdInvalidException;

@Service
public class UseCaseService {
    private final UseCaseRepository useCaseRepository;
    private final CampaignService campaignService;

    public UseCaseService(UseCaseRepository useCaseRepository, CampaignService campaignService) {
        this.useCaseRepository = useCaseRepository;
        this.campaignService = campaignService;
    }

    public ResultPaginationDTO fetchAll(Specification<UseCase> spec, Pageable pageable) {
        Page<UseCase> pageUser = this.useCaseRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageUser.getContent());

        return rs;
    }

    public UseCase createUseCase(UseCase useCase) {
        if (useCase.getCampaign() != null) {
            Campaign campaign = this.campaignService.fetchCampaignById(useCase.getCampaign().getId());
            useCase.setCampaign(campaign != null ? campaign : null);
        }
        return useCaseRepository.save(useCase);
    }

    public UseCase fetchUseCaseById(Long id) {
        return useCaseRepository.findById(id).orElse(null);
    }

    public UseCase updateUseCase(UseCase useCase) {
        UseCase existingUseCase = this.useCaseRepository.findById(useCase.getId()).orElse(null);

        if (existingUseCase == null) {
            throw new IdInvalidException("Use case with id = " + useCase.getId() + " does not exist.");
        }
        existingUseCase.setName(useCase.getName());
        existingUseCase.setDescription(useCase.getDescription());
        if (useCase.getCampaign() != null) {
            Campaign campaign = this.campaignService.fetchCampaignById(useCase.getCampaign().getId());
            existingUseCase.setCampaign(campaign != null ? campaign : null);
        }

        return useCaseRepository.save(existingUseCase);
    }

    public void deleteUseCase(Long id) {
        this.useCaseRepository.deleteById(id);
    }
}
