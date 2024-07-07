var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "4362",
        "ok": "2177",
        "ko": "2185"
    },
    "minResponseTime": {
        "total": "3",
        "ok": "79",
        "ko": "3"
    },
    "maxResponseTime": {
        "total": "298",
        "ok": "298",
        "ko": "65"
    },
    "meanResponseTime": {
        "total": "93",
        "ok": "181",
        "ko": "5"
    },
    "standardDeviation": {
        "total": "90",
        "ok": "30",
        "ko": "3"
    },
    "percentiles1": {
        "total": "43",
        "ok": "179",
        "ko": "5"
    },
    "percentiles2": {
        "total": "179",
        "ok": "203",
        "ko": "5"
    },
    "percentiles3": {
        "total": "218",
        "ok": "226",
        "ko": "6"
    },
    "percentiles4": {
        "total": "234",
        "ok": "241",
        "ko": "11"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2177,
    "percentage": 49.90829894543787
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 2185,
    "percentage": 50.09170105456213
},
    "meanNumberOfRequestsPerSecond": {
        "total": "140.71",
        "ok": "70.23",
        "ko": "70.48"
    }
},
contents: {
"req_register--625569085": {
        type: "REQUEST",
        name: "Register",
path: "Register",
pathFormatted: "req_register--625569085",
stats: {
    "name": "Register",
    "numberOfRequests": {
        "total": "2181",
        "ok": "2177",
        "ko": "4"
    },
    "minResponseTime": {
        "total": "52",
        "ok": "79",
        "ko": "52"
    },
    "maxResponseTime": {
        "total": "298",
        "ok": "298",
        "ko": "65"
    },
    "meanResponseTime": {
        "total": "180",
        "ok": "181",
        "ko": "59"
    },
    "standardDeviation": {
        "total": "30",
        "ok": "30",
        "ko": "5"
    },
    "percentiles1": {
        "total": "179",
        "ok": "179",
        "ko": "59"
    },
    "percentiles2": {
        "total": "203",
        "ok": "203",
        "ko": "62"
    },
    "percentiles3": {
        "total": "226",
        "ok": "226",
        "ko": "64"
    },
    "percentiles4": {
        "total": "241",
        "ok": "241",
        "ko": "65"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2177,
    "percentage": 99.81659789087574
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 4,
    "percentage": 0.18340210912425492
},
    "meanNumberOfRequestsPerSecond": {
        "total": "70.35",
        "ok": "70.23",
        "ko": "0.13"
    }
}
    },"req_create-vehicle-1798009864": {
        type: "REQUEST",
        name: "Create Vehicle",
path: "Create Vehicle",
pathFormatted: "req_create-vehicle-1798009864",
stats: {
    "name": "Create Vehicle",
    "numberOfRequests": {
        "total": "2181",
        "ok": "0",
        "ko": "2181"
    },
    "minResponseTime": {
        "total": "3",
        "ok": "-",
        "ko": "3"
    },
    "maxResponseTime": {
        "total": "29",
        "ok": "-",
        "ko": "29"
    },
    "meanResponseTime": {
        "total": "5",
        "ok": "-",
        "ko": "5"
    },
    "standardDeviation": {
        "total": "1",
        "ok": "-",
        "ko": "1"
    },
    "percentiles1": {
        "total": "5",
        "ok": "-",
        "ko": "5"
    },
    "percentiles2": {
        "total": "5",
        "ok": "-",
        "ko": "5"
    },
    "percentiles3": {
        "total": "6",
        "ok": "-",
        "ko": "6"
    },
    "percentiles4": {
        "total": "9",
        "ok": "-",
        "ko": "9"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0.0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 0,
    "percentage": 0.0
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 2181,
    "percentage": 100.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "70.35",
        "ok": "-",
        "ko": "70.35"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}
