'use strict';

angular.module('taxiwizzApp')
    .controller('BookingDetailController', function ($scope, $stateParams, Booking, User) {
        $scope.booking = {};
        $scope.load = function (id) {
            Booking.get({id: id}, function(result) {
              $scope.booking = result;
            });
        };
        $scope.load($stateParams.id);
    });
