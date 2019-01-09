const ROOT_PATH = "http://localhost:8096/";

var mainModule = angular.module('mainModule', [ 'ui.router', 'ngStorage', 'angular-jwt' ]);

mainModule.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/home');

    $stateProvider.state('home', {
        url: '/home',
        templateUrl : 'appParts/home/home.html'
    })
    .state('login', {
        url: '/login',
        templateUrl : 'appParts/login/login.html',
        controller : 'loginController'
    })

});