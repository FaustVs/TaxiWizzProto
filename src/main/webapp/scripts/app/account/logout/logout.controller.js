'use strict';

angular.module('taxiwizzApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
