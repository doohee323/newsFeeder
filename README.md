NewsFeeder
===========

ㅇ Function
	- parsing feed with New York Time API
	- list and detail web page on webview
	- support getting more item after scrolling
	- caching data in sqllight
	- made web page with angular.js  

ㅇ Development process
	1. analyze New York Time API's json format
	2. define UI flow and display columns
	3. develop UI with sample JSON
	4. develop android app
	5. optimize UI resource with grunt build
	
ㅇ Application flow
	- start android app
	- create sqllight db / table
	- load webview
	- call the request for getting the json from api on webpage's load script
		/NewsFeeder/assets/www/scripts/controllers/list.js
			-> $scope.retrieve
				-> MainService
					-> Android.getStoryList(JSON.stringify(param));
		/NewsFeeder/src/com/tz/newsfeeder/service/FromWebService.java
			-> return new StoryDao(mContext).getStoryList(last_updated, page).toString();
				-> getFeeds(last_updated);
					if not exist,
					-> AppUtil.getjson(SERVICE_URL).getJSONArray("results");
	- When scrolling down to the bottom, call the request for getting more items
		/NewsFeeder/src/com/tz/newsfeeder/service/WebViewService.java
			-> myWebView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
		/NewsFeeder/assets/www/scripts/controllers/list.js
			-> var gfNextPage = function(json) {
	- After selecting one item, change to the detail screen
		
ㅇ To-do
	- I didn't defind the meaning of api url and last_updated
		/NewsFeeder/src/com/tz/newsfeeder/dao/StoryDao.java
			final static String SERVICE_URL = "http://api.nytimes.com/svc/topstories/v1/home.json?api-key=201f2d9fc5a0f555ac4ad2946c51950d:7:71840120";

ㅇ projects
	- android app:
	- web app:
	
	
	
	
	


