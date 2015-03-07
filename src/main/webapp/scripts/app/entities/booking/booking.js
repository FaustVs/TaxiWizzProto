'use strict';

angular.module('taxiwizzApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('booking', {
                parent: 'entity',
                url: '/booking',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'taxiwizzApp.booking.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/booking/bookings.html',
                        controller: 'BookingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('booking');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bookingDetail', {
                parent: 'entity',
                url: '/booking/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'taxiwizzApp.booking.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/booking/booking-detail.html',
                        controller: 'BookingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('booking');
                        return $translate.refresh();
                    }]
                }
            });
    });
