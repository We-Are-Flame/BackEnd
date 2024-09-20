package com.backend.meeting.domain.category.implemantation;

import com.backend.meeting.domain.category.entity.Category;
import com.backend.before.exception.ErrorMessages;
import com.backend.before.exception.NotFoundException;
import com.backend.meeting.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategorySearcher {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NotFoundException(ErrorMessages.CATEGORY_NOT_FOUND));
    }
}
