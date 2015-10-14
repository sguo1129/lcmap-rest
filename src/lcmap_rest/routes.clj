(ns lcmap-rest.routes
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer [GET context defroutes]]
            [compojure.route :as route]
            [lcmap-client.core]
            [lcmap-client.l8.surface-reflectance]
            [lcmap-rest.l8.surface-reflectance :as l8-sr]
            [lcmap-rest.management :as management]
            [lcmap-rest.util :as util]))

(defroutes surface-reflectance
  (context lcmap-client.l8.surface-reflectance/context []
    (GET "/" request
      (l8-sr/get-resources (:uri request)))
    (GET "/tiles" [point extent time band :as request]
      (l8-sr/get-tiles point extent time band request))
    (GET "/rod" [point time band :as request]
      (l8-sr/get-rod point time band request))))

;; XXX this needs to go into a protected area; see this ticket:
;;  https://my.usgs.gov/jira/browse/LCMAP-71
(defroutes management
  (context "/manage" []
    (GET "/status" [] (management/get-status))
    ;; XXX add more management resources
    ))

(defroutes v0
  surface-reflectance
  management
  (route/not-found "Resource not found"))

(defroutes v1
  management
  (route/not-found "Resource not found"))

(defroutes v2
  management
  (route/not-found "Resource not found"))

