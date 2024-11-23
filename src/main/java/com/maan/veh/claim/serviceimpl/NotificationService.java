package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.MailMaster;
import com.maan.veh.claim.entity.NotifTemplateMaster;
import com.maan.veh.claim.entity.NotifTransactionDetails;
import com.maan.veh.claim.notification.Mail;
import com.maan.veh.claim.notification.MailJob;
import com.maan.veh.claim.notification.PushedStateChange;
import com.maan.veh.claim.repository.MailMasterRepository;
import com.maan.veh.claim.repository.NotifTemplateMasterRepository;

@Service
public class NotificationService {

	@Value(value = "${kafka.push.mail}")
	private String kafkaLink;

	@Autowired
	private MailMasterRepository mailRepo;

	@Autowired
	private NotifTemplateMasterRepository masterRepo;

	public void jobProcess(List<NotifTransactionDetails> transDetails) {

		List<List<Object>> collect = null;
		Date d = new Date();
		if (transDetails.size() > 0) {
			try {
				transDetails.stream().forEach(tr -> tr.setNotifPushedStatus("Y"));

				Map<String, Map<Integer, Map<String, List<NotifTransactionDetails>>>> groups = transDetails.stream()
						.collect(Collectors.groupingBy(NotifTransactionDetails::getCompanyid,
								Collectors.groupingBy(NotifTransactionDetails::getProductid,
										Collectors.groupingBy(NotifTransactionDetails::getNotifTemplatename))));

				synchronized (transDetails) {

					for (Entry<String, Map<Integer, Map<String, List<NotifTransactionDetails>>>> g : groups
							.entrySet()) {
						Map<Integer, Map<String, List<NotifTransactionDetails>>> h = g.getValue();
						for (Entry<Integer, Map<String, List<NotifTransactionDetails>>> h1 : h.entrySet()) {
							Map<String, List<NotifTransactionDetails>> h2 = h1.getValue();
							for (Entry<String, List<NotifTransactionDetails>> h3 : h2.entrySet()) {

								List<NotifTransactionDetails> n = h3.getValue();
								List<NotifTemplateMaster> templat = masterRepo
										.findByCompanyIdAndProductIdAndStatusAndNotifTemplatenameIgnoreCaseOrderByAmendIdDesc(
												n.get(0).getCompanyid(), Long.valueOf(n.get(0).getProductid()), "Y",
												n.get(0).getNotifTemplatename());
								if (!templat.isEmpty()) {

									List<MailMaster> mailc = mailRepo
											.findByCompanyIdAndBranchCodeAndStatusOrderByAmendIdDesc(
													n.get(0).getCompanyid(), "99999", "Y");

									PushedStateChange p = new PushedStateChange(templat.get(0), mailc.get(0));
									collect = n.stream().map(p).filter(dd -> dd != null).collect(Collectors.toList());
									List<Mail> totalMailJob = new ArrayList<Mail>();

									if (!collect.isEmpty()) {
										for (List<Object> list : collect) {
											// totalJob.addAll(list);
											for (Object o : list) {

												if (o instanceof Mail) {
													totalMailJob.add((Mail) o);
												}
											}
										}
										if (!totalMailJob.isEmpty()) {
											MailJob job = new MailJob(kafkaLink);
											totalMailJob.stream().forEach(job);
										}

									}
								}

							}
						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}
}
