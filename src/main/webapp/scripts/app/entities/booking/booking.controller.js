'use strict';

angular.module('taxiwizzApp')
    .controller('BookingController', function ($scope, Booking, User) {
        $scope.bookings = [];
        $scope.users = User.query();
        $scope.loadAll = function() {
            Booking.query(function(result) {
               $scope.bookings = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Booking.update($scope.booking,
                function () {
                    $scope.loadAll();
                    $('#saveBookingModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Booking.get({id: id}, function(result) {
                $scope.booking = result;
                $('#saveBookingModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Booking.get({id: id}, function(result) {
                $scope.booking = result;
                $('#deleteBookingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Booking.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBookingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.booking = {destination: null, departure: null, id: null};
        };
    });
