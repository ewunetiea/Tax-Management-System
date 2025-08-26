package com.afr.fms.Common.Recommendation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.afr.fms.Admin.Entity.User;
import com.afr.fms.Admin.Mapper.UserMapper;
import com.afr.fms.Common.RecentActivity.RecentActivity;
import com.afr.fms.Common.RecentActivity.RecentActivityMapper;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationMapper recommendationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecentActivityMapper recentActivityMapper;
    private RecentActivity recentActivity;

    public void createRecommendation(Recommendation recommendation) {
        recommendationMapper.createRecommendation(recommendation);

        User user = userMapper.getAuditorById(recommendation.getUser().getId());

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Common recommendation is created");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);

    }

    public void updateRecommendation(Recommendation recommendation) {
        User user = userMapper.getAuditorById(recommendation.getUser().getId());

        recommendationMapper.updateRecommendation(recommendation);

        recentActivity = new RecentActivity();
        recentActivity.setMessage("Common recommendation is updated");
        user.setId(user.getId());
        recentActivity.setUser(user);
        recentActivityMapper.addRecentActivity(recentActivity);
    }

    public List<Recommendation> getRecommendation(Long user_id) {
        return recommendationMapper.getRecommendation(user_id);
    }

    public List<Recommendation> getRecommendations(String identifier) {
        return recommendationMapper.getRecommendations(identifier);
    }

    public void deleteRecommendation(Long id) {
        recommendationMapper.deleteRecommendation(id);

    }

}
