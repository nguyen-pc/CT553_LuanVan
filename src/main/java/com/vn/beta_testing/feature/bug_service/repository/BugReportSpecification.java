package com.vn.beta_testing.feature.bug_service.repository;

import com.vn.beta_testing.domain.BugReport;
import com.vn.beta_testing.util.constant.PriorityEnum;
import com.vn.beta_testing.util.constant.SeverityEnum;
import com.vn.beta_testing.util.constant.StatusBugEnum;
import org.springframework.data.jpa.domain.Specification;

public class BugReportSpecification {

    public static Specification<BugReport> hasTester(Long testerId) {
        return (root, query, cb) -> testerId == null
                ? cb.conjunction()
                : cb.equal(root.get("tester").get("id"), testerId);
    }

    public static Specification<BugReport> hasAssignee(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    public static Specification<BugReport> hasBugType(Long bugTypeId) {
        return (root, query, cb) -> bugTypeId == null
                ? cb.conjunction()
                : cb.equal(root.get("bugType").get("id"), bugTypeId);
    }

    public static Specification<BugReport> hasCampaign(Long campaignId) {
        return (root, query, cb) -> campaignId == null
                ? cb.conjunction()
                : cb.equal(root.get("campaign").get("id"), campaignId);
    }

    public static Specification<BugReport> hasStatus(StatusBugEnum status) {
        return (root, query, cb) -> status == null
                ? cb.conjunction()
                : cb.equal(root.get("status"), status);
    }

    public static Specification<BugReport> hasPriority(PriorityEnum priority) {
        return (root, query, cb) -> priority == null
                ? cb.conjunction()
                : cb.equal(root.get("priority"), priority);
    }

    public static Specification<BugReport> hasSeverity(SeverityEnum severity) {
        return (root, query, cb) -> severity == null
                ? cb.conjunction()
                : cb.equal(root.get("severity"), severity);
    }
}