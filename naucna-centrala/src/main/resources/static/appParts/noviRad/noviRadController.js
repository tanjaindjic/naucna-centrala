(function () {
    'use strict';
    mainModule.controller('noviRadController', ['$scope', '$http', '$window', '$localStorage', '$location', '$stateParams', 'mainService',
        function ($scope, $http, $location, $window, $localStorage, $stateParams, mainService) {


            var init = function () {
                console.log("Usao u init novog rada");
            }
            init();



        }
    ]);
})();