package com.webencyclop.demo.controller;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.webencyclop.demo.model.*;
import com.webencyclop.demo.repository.*;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.apache.solr.client.solrj.SolrQuery;

import com.webencyclop.demo.securityservice.UserService;

import java.io.*;
import java.util.*;


@Controller
public class AuthenticationController {

	@Autowired
	UserService userService;

	@Autowired
	VideoRepository videoRepository;

	@Autowired
	RatingRepository ratingRepository;

	@Autowired
	VideoSolrRepository videoSolrRepository;

	@Autowired
	RatingCriteriaRepository ratingCriteriaRepository;

	@Autowired
	EvaluationCriteriaRepository evaluationCriteriaRepository;

	@Autowired
	EvaluationRepository evaluationRepository;

	private SolrClient solrClient = new HttpSolrClient.Builder("http://localhost:8983/solr/videos").build();



	/*
	 *   This method return login page. This should be the first page a user will visit when he or she
	 *   entered into this website.
	 */
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login"); // resources/template/login.html
		return modelAndView;
	}

	/*
	 *This is the page site_user will get when they are authenticated (need to be change.)
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home"); // resources/template/home.html
		return modelAndView;
	}

	/*
	 * This page is available for users who have a
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public ModelAndView adminHome() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin"); // resources/template/admin.html
		List<RatingCriteria> allCriteria = ratingCriteriaRepository.findAll();

		List<EvaluationCriteria> evaluationCriteriaList = evaluationCriteriaRepository.findAll();
//		for(RatingCriteria singleOne : allCriteria){
//			System.out.println(singleOne.toString());
//		}

		modelAndView.addObject("options",allCriteria);
		modelAndView.addObject("criteria",evaluationCriteriaList);
		return modelAndView;
	}



	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ModelAndView registerUser(@Valid User user, BindingResult bindingResult, ModelMap modelMap) {
		ModelAndView modelAndView = new ModelAndView();
		// Check for the validations
		if(bindingResult.hasErrors()) {
			modelAndView.addObject("successMessage", "Please correct the errors in form!");
			modelMap.addAttribute("bindingResult", bindingResult);
		}
		else if(userService.isUserAlreadyPresent(user)){
			modelAndView.addObject("successMessage", "user already exists!");
		}
		//  Will save the user if, no binding errors
		else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User is registered successfully!");
		}
		modelAndView.addObject("user", new User());
		modelAndView.setViewName("register");
		return modelAndView;
	}


	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("register"); // resources/template/register.html
		return modelAndView;
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView uploadVideopage(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("upload");
		return modelAndView;

	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam("file") MultipartFile file, String subtitle, String abstracts, Authentication authentication) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("failedtoupload");
		// find out who upload this video
		String uploader = authentication.getName();


		// save video information to database
		Video video = new Video();
		video.setTitle(subtitle);
		video.setAbstracts(abstracts);
		video.setVideoAddress("http://10.84.2.157/" + uploader +file.getOriginalFilename());
		video.setUploadBy(uploader);
		videoRepository.save(video);

		// save video information to Solr
		VideoSolr videoSolr = new VideoSolr();
		videoSolr.setTitle(subtitle);
		videoSolr.setAbstracts(abstracts);
		videoSolr.setVideoAddress("http://10.84.2.157/" + uploader +file.getOriginalFilename());
		videoSolr.setUploadBy(uploader);
		videoSolrRepository.save(videoSolr);


		if (!file.isEmpty()) {
			try {

				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(new File( "/Users/bocui/Downloads/CSE611-master/src/main/resources/video/"+uploader +file.getOriginalFilename())));
				out.write(file.getBytes());
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return modelAndView;
			} catch (IOException e) {
				e.printStackTrace();
				return modelAndView;
			}
			modelAndView.setViewName("uploaded");
			return modelAndView;
		} else {
			return modelAndView;
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	@JsonIgnore
	public ArrayList<Video> searchQuery(String userEntered) throws IOException, SolrServerException {
//		System.out.println(userEntered);

		//query Solr based on user entered info.
		SolrQuery query = new SolrQuery("defType", "dismax");
		query.add("qf","title","abstracts");
		query.add("q",userEntered);
		QueryResponse response = solrClient.query(query);
//		System.out.println(response.getResults());
		SolrDocumentList rs = response.getResults();

		//prepare return information to the front end.
		ArrayList<Video> answer = new ArrayList<>();
		long numFound = rs.getNumFound();
		int current = 0;
		while(current < numFound){
			ListIterator <SolrDocument> it = rs.listIterator();
			//for a single document
			while(it.hasNext()){
				Video temp =new Video();
				current ++;
//				System.out.println("*************  "+ current +" in "+ numFound + " ***************");
				SolrDocument doc = it.next();
				Map<String, Collection<Object>> values = doc.getFieldValuesMap();
				ArrayList<String> h1 = (ArrayList)doc.get("title");
				temp.setTitle(h1.get(0));

				ArrayList<String> h2 = (ArrayList)doc.get("abstracts");
				temp.setAbstracts(h2.get(0));

				ArrayList<String> h3 = (ArrayList)doc.get("uploadBy");
				temp.setUploadBy(h3.get(0));

				ArrayList<String> h4 = (ArrayList)doc.get("videoAddress");
				temp.setVideoAddress(h4.get(0));


				answer.add(temp);
				//Print search result to test
//				Iterator<String> names = doc.getFieldNames().iterator();
//				while(names.hasNext()){
//					String name = names.next();
//					System.out.print(name);
//					System.out.print(" = ");
//
//					Collection<Object> vals = values.get(name);
//					Iterator<Object> valsIter = vals.iterator();
//					while (valsIter.hasNext()) {
//						Object obj = valsIter.next();
//						System.out.println(obj.toString());
//					}
//
//				}
			}
		}


		return answer;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView giveSearchPage(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("search");
		return modelAndView;
	}


	@RequestMapping(value = "/watch", method = RequestMethod.GET)
	public ModelAndView giveWatchPage(String requestedAddress){
		Video video = videoRepository.getVideoByVideoAddress(requestedAddress);
		List<RatingCriteria> allCriteria = ratingCriteriaRepository.findAll();

//		for(RatingCriteria one :allCriteria){
//			System.out.println(one);
//		}


		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("watch");
		modelAndView.addObject("title",video.getTitle());
		modelAndView.addObject("abstracts",video.getAbstracts());
		modelAndView.addObject("address",requestedAddress);
		modelAndView.addObject("address2","/evaluation?requestedAddress="+requestedAddress);
		modelAndView.addObject("options",allCriteria);

		return modelAndView;
	}

	@RequestMapping(value = "/saveRating", method = RequestMethod.POST)
	@ResponseBody
	public String saveRating(String evaluation, String videoAddress,String attribute,Authentication authentication){
		String userName = authentication.getName();
		System.out.println(evaluation);
		System.out.println(videoAddress);
		Rating rating = ratingRepository.findByVideoAddressUserAndAttribute(videoAddress,userName,attribute);
		if(rating != null){
			rating.setRating(evaluation);
			ratingRepository.save(rating);
		}else{
			rating = new Rating();
			rating.setRating(evaluation);
			rating.setRatedBy(userName);
			rating.setVideoAddress(videoAddress);
			rating.setAttribute(attribute);
			ratingRepository.save(rating);

		}

		return "OK";
	}

	@RequestMapping(value = "/getratings",method = RequestMethod.GET)
	@ResponseBody
	public List<Rating> getAllRarings(String requestedAddress, String attribute){
//		System.out.println(attribute);
//		System.out.println(requestedAddress);
		List<Rating> temp = ratingRepository.findAllByVideoAddressAndAttribute(requestedAddress,attribute);
//		for(Rating i : temp){
//			System.out.println(i.getRating());
//		}
		return temp;
	}

	/*
	   Delete one criterion by admin's choice. And return all criteria to update the front end table
	 */

	@RequestMapping(value = "deleteCriterion",method = RequestMethod.GET)
	@ResponseBody
	public List<RatingCriteria> deleteOneCriterion( String criterion){

		System.out.println(criterion);

		RatingCriteria ratingCriteria = ratingCriteriaRepository.findByOneCriterion(criterion);

		if(ratingCriteria!= null){
			ratingCriteriaRepository.delete(ratingCriteria);
		}
		List<RatingCriteria> allCriteria = ratingCriteriaRepository.findAll();

		return allCriteria;

	}

	@RequestMapping(value ="Deleted",method = RequestMethod.GET)
	public ModelAndView deleted(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("deleted");

		return modelAndView;
	}
//

//	@RequestMapping(value = "deleteCriterion",method = RequestMethod.GET)
//	@ResponseBody
//	public ModelAndView deleteOneCriterion( String criterion){
//
//
//		System.out.println(criterion);
//
//		RatingCriteria ratingCriteria = ratingCriteriaRepository.findByOneCriterion(criterion);
//
//		if(ratingCriteria!= null){
//			ratingCriteriaRepository.delete(ratingCriteria);
//		}
//
//
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("admin"); // resources/template/admin.html
//		List<RatingCriteria> allCriteria = ratingCriteriaRepository.findAll();
//		for(RatingCriteria singleOne : allCriteria){
//			System.out.println(singleOne.toString());
//		}
//
//		modelAndView.addObject("options",allCriteria);
//		return modelAndView;
//
//
//	}

	@RequestMapping(value = "addCriterion",method = RequestMethod.GET)
	@ResponseBody
	public String addOneCriterion( String criterion){

		System.out.println(criterion);
		RatingCriteria ratingCriteria = new RatingCriteria();
		ratingCriteria.setOneCriterion(criterion);
		ratingCriteriaRepository.save(ratingCriteria);


		return "ok";

	}

	@RequestMapping(value ="Added",method = RequestMethod.GET)
	public ModelAndView added(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("added");

		return modelAndView;
	}



	/*
		Http handler for evaluation model
	 */

	@RequestMapping(value = "evaluation",method = RequestMethod.GET)
	public ModelAndView getEvaluation(String requestedAddress){
		Video video = videoRepository.getVideoByVideoAddress(requestedAddress);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("evaluation");
		modelAndView.addObject("address",requestedAddress);
		modelAndView.addObject("address2","/watch?requestedAddress="+requestedAddress);
		modelAndView.addObject("title",video.getTitle());
		modelAndView.addObject("abstracts",video.getAbstracts());


		return modelAndView;
	}

	@RequestMapping(value = "EvaluationCriteria", method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationCriteria> getAllEvaluationCriteria(){
		List<EvaluationCriteria> all = evaluationCriteriaRepository.findAll();

		return all;
	}

	@RequestMapping(value = "saveEvaluation",method = RequestMethod.POST)
	@ResponseBody
	public String saveEvaluation(@RequestParam(value = "evaluation[]") String[] evaluation, String address,Authentication authentication ){
		for(String i : evaluation){
			System.out.println(i);
		}
//		System.out.println(address);
		String userName = authentication.getName();
		for(int i=0; i<evaluation.length;i++){
			Evaluation single = evaluationRepository.findByVideoAddressUserAndCriterion(address,userName,evaluation[i]);
			if(single ==null){
				Evaluation need = new Evaluation();
				need.setVideoAddress(address);
				need.setCriterion(evaluation[i]);
				need.setRatedBy(userName);
				need.setLevel(evaluation[i+1]);
				i++;
				evaluationRepository.save(need);
			}else{
				single.setLevel(evaluation[i+1]);
				evaluationRepository.save(single);
				i++;
			}
		}

		return "ok";

	}


	@RequestMapping(value = "deleteEvaluationCriterion",method = RequestMethod.GET)
	@ResponseBody
	public List<EvaluationCriteria> deleteOneEvaluationCriterion( String criterion){

		System.out.println(criterion);

		EvaluationCriteria evaluationCriteria = evaluationCriteriaRepository.findByOneCriterion(criterion);

		if(evaluationCriteria!= null){
			evaluationCriteriaRepository.delete(evaluationCriteria);
		}
		List<EvaluationCriteria> allCriteria = evaluationCriteriaRepository.findAll();

		return allCriteria;

	}



	@RequestMapping(value = "addEvaluation",method = RequestMethod.GET)
	@ResponseBody
	public String addOneEvaluationCriterion( String criterion){

		System.out.println(criterion);
		EvaluationCriteria evaluationCriteria = new EvaluationCriteria();
		evaluationCriteria.setOneCriterion(criterion);
		evaluationCriteriaRepository.save(evaluationCriteria);


		return "ok";

	}




	@RequestMapping(value = "getOtherEvaluation",method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<List<Evaluation>> getOtherEvaluation(String address){
//		System.out.println(address);
		List <EvaluationCriteria> evaluationCriteriaList = evaluationCriteriaRepository.findAll();

		ArrayList<List<Evaluation>> ret = new ArrayList<>();
		for(EvaluationCriteria single: evaluationCriteriaList){
			List<Evaluation> temp = evaluationRepository.findByVideoAddressAndCriterion(address, single.getOneCriterion());
			ret.add(temp);
		}


		return ret;

	}
}



