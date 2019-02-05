const ROOT_PATH = "https://localhost:8096/";
const ELASTIC_PATH = "http://localhost:9200/";
const JWT_TOKEN = "token";

var mainModule = angular.module('mainModule', [ 'ui.router', 'ngStorage', 'angular-jwt' ]);

mainModule.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/home');

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
     .state('noviRad', {
        url: '/noviRad',
        templateUrl : 'appParts/noviRad/noviRad.html',
        controller : 'noviRadController'
    })
    .state('core.casopis', {
        url: 'casopis/{id}',
        params: {
                    'id' : undefined
                },
        templateUrl : 'appParts/casopis/casopis.html',
        controller : 'casopisController'
    })

    .state('core.rad', {
        url: 'rad/{id}',
        templateUrl : 'appParts/rad/rad.html',
        controller : 'radController'
    })

});