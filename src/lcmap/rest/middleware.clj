(ns lcmap.rest.middleware
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [clojusc.ring.xml :as ring-xml]
            [metrics.ring.expose :as ring-metrics]
            [metrics.ring.instrument :as ring-instrument]
            [lcmap.client.system]
            [lcmap.rest.api.routes :as routes]
            [lcmap.rest.middleware.content-type :as content-type]
            [lcmap.rest.middleware.versioned-api :as versioned-api]))

(defn lcmap-handlers
  "This function provides the LCMAP REST server with the single means by which
  the application pulls in all LCMAP Ring handers. Any new handlers that are
  created should be chained here and not in ``lcmap.rest.app``."
  [default-version]
  (-> (versioned-api/handler default-version)
      (content-type/handler default-version)
      (ring-metrics/expose-metrics-as-json
        (str lcmap.client.system/context "/metrics/all"))
      (ring-instrument/instrument)))