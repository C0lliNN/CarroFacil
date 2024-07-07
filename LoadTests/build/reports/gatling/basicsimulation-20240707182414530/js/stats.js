var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name--1146707516",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "4416",
        "ok": "2206",
        "ko": "2210"
    },
    "minResponseTime": {
        "total": "2",
        "ok": "77",
        "ko": "2"
    },
    "maxResponseTime": {
        "total": "289",
        "ok": "289",
        "ko": "53"
    },
    "meanResponseTime": {
        "total": "92",
        "ok": "179",
        "ko": "5"
    },
    "standardDeviation": {
        "total": "90",
        "ok": "29",
        "ko": "2"
    },
    "percentiles1": {
        "total": "58",
        "ok": "179",
        "ko": "4"
    },
    "percentiles2": {
        "total": "179",
        "ok": "199",
        "ko": "5"
    },
    "percentiles3": {
        "total": "215",
        "ok": "222",
        "ko": "6"
    },
    "percentiles4": {
        "total": "234",
        "ok": "243",
        "ko": "10"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2206,
    "percentage": 49.95471014492754
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
    "count": 2210,
    "percentage": 50.04528985507246
},
    "meanNumberOfRequestsPerSecond": {
        "total": "142.45",
        "ok": "71.16",
        "ko": "71.29"
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
        "total": "2208",
        "ok": "2206",
        "ko": "2"
    },
    "minResponseTime": {
        "total": "48",
        "ok": "77",
        "ko": "48"
    },
    "maxResponseTime": {
        "total": "289",
        "ok": "289",
        "ko": "53"
    },
    "meanResponseTime": {
        "total": "179",
        "ok": "179",
        "ko": "51"
    },
    "standardDeviation": {
        "total": "29",
        "ok": "29",
        "ko": "3"
    },
    "percentiles1": {
        "total": "179",
        "ok": "179",
        "ko": "51"
    },
    "percentiles2": {
        "total": "199",
        "ok": "199",
        "ko": "52"
    },
    "percentiles3": {
        "total": "222",
        "ok": "222",
        "ko": "53"
    },
    "percentiles4": {
        "total": "243",
        "ok": "243",
        "ko": "53"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 2206,
    "percentage": 99.90942028985508
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
    "count": 2,
    "percentage": 0.09057971014492754
},
    "meanNumberOfRequestsPerSecond": {
        "total": "71.23",
        "ok": "71.16",
        "ko": "0.06"
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
        "total": "2208",
        "ok": "0",
        "ko": "2208"
    },
    "minResponseTime": {
        "total": "2",
        "ok": "-",
        "ko": "2"
    },
    "maxResponseTime": {
        "total": "30",
        "ok": "-",
        "ko": "30"
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
        "total": "4",
        "ok": "-",
        "ko": "4"
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
        "total": "10",
        "ok": "-",
        "ko": "10"
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
    "count": 2208,
    "percentage": 100.0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "71.23",
        "ok": "-",
        "ko": "71.23"
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
