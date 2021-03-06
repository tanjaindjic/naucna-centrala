const ROOT_PATH = "https://localhost:8096/";
const ELASTIC_PATH = "http://localhost:9200/";
const JWT_TOKEN = "token";

var mainModule = angular.module('mainModule', [ 'ui.router', 'ngStorage', 'angular-jwt','ngPDFViewer', 'ngSanitize' ]);

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
    .state('core.login', {
        url: 'login',
        templateUrl : 'appParts/login/login.html',
        controller : 'loginController'
    })
     .state('core.register', {
        url: 'register',
        templateUrl : 'appParts/register/register.html',
        controller : 'registerController'
    })
     .state('core.noviRad', {
        url: 'noviRad',
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
    .state('core.paymentFailed', {
        url: 'paymentFailed',
        templateUrl : 'appParts/paymentFailed/paymentFailed.html',
        controller : 'paymentFailedController'
    })
    .state('core.paymentSuccess', {
        url: 'paymentSuccess',
        templateUrl : 'appParts/paymentSuccess/paymentSuccess.html',
        controller : 'paymentSuccessController'
    })
    .state('core.paymentError', {
        url: 'paymentError',
        templateUrl : 'appParts/paymentError/paymentError.html',
        controller : 'paymentErrorController'
    })
    .state('core.profil', {
        url: 'profil',
        templateUrl : 'appParts/profil/profil.html',
        controller : 'profilController'
    })
    .state('core.tasks', {
        url: 'tasks',
        templateUrl : 'appParts/tasks/tasks.html',
        controller : 'tasksController'
    })
    .state('core.dodajRecenzenta', {
        url: 'dodajRecenzenta/{id}',
        templateUrl : 'appParts/dodajRecenzenta/dodajRecenzenta.html',
        controller : 'dodajRecenzentaController'
    })
    .state('core.prijavljeniRadovi', {
        url: 'prijavljeniRadovi',
        templateUrl : 'appParts/prijavljeniRadovi/prijavljeniRadovi.html',
        controller : 'prijavljeniRadoviController'
    })
    .state('core.recenzije', {
        url: 'recenzije',
        templateUrl : 'appParts/recenzije/recenzije.html',
        controller : 'recenzijeController'
    })

});