(function() { "use strict";

    mainModule.controller('homeController', [ '$scope', '$http', '$window','$localStorage','$location', '$stateParams','$state','mainService',
        function($scope,  $http, $location, $window, $localStorage, $stateParams, $state, mainService) {
            

            var init = function(){
                if (!mainService.getJwtToken()) {
                    mainService.goToState("login")
    			}else $scope.sub = mainService.getSub();

            }
            init();

        }
    ]);
})();