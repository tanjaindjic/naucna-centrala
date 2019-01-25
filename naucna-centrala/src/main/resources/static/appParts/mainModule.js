const ROOT_PATH = "http://localhost:8096/";
const JWT_TOKEN = "token";

var mainModule = angular.module('mainModule', [ 'ui.router', 'ngStorage', 'angular-jwt' ]);

mainModule.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider
    .state('core', {
			 url: '/',
			 templateUrl: 'appParts/core/core.html',
			 controller: 'coreController'
		 })
    .state('core.home', {
        url: 'home',
        templateUrl : 'appParts/home/home.html',
        controller : 'homeController'
    })
    .state('login', {
        url: '/login',
        templateUrl : 'appParts/login/login.html',
        controller : 'loginController'
    })
     .state('register', {
        url: '/register',
        templateUrl : 'appParts/register/register.html',
        controller : 'registerController'
    })

});