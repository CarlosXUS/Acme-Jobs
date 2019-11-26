
package acme.features.authenticated.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedJobShowService implements AbstractShowService<Authenticated, Job> {

	@Autowired
	AuthenticatedJobRepository repository;


	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;
		boolean result;
		Integer jobId;
		Principal principal;
		principal = request.getPrincipal();
		Job job;
		jobId = request.getModel().getInteger("id");
		job = this.repository.findOneById(jobId);

		if (principal.hasRole(Employer.class)) {
			Employer employer;
			employer = job.getEmployer();
			result = job.getStatus().equals("published") || !job.getStatus().equals("published") && employer.getUserAccount().getId() == principal.getAccountId();
		} else {
			result = job.getStatus().equals("published");
		}
		return result;
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model, "referenceNumber", "status", "title", "deadline", "salary", "moreInfo", "description");

	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;
		Job result;
		int id;
		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);
		return result;

	}

}
