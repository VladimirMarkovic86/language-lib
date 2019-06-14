(ns language-lib.core
  (:require [session-lib.core :as ssn]
            [mongo-lib.core :as mon]
            [dao-lib.core :as dao]
            [ajax-lib.http.entity-header :as eh]
            [ajax-lib.http.mime-type :as mt]
            [ajax-lib.http.status-code :as stc]
            [common-middle.collection-names :refer [language-cname
                                                    preferences-cname
                                                    session-cname
                                                    long-session-cname]]))

(defn get-labels
  "Read labels for chosen language"
  [request]
  (let [language (atom
                   (ssn/get-accept-language
                     request))]
    (let [preferences (ssn/get-preferences
                        request)]
      (when (:language preferences)
        (reset!
          language
          (:language preferences))
       ))
    (let [entity-type language-cname
          entity-filter {}
          projection-vector [:code @language]
          projection-include true
          projection (dao/build-projection
                       projection-vector
                       projection-include)
          qsort {:code 1}
          collation {:locale "sr"}
          db-result (mon/mongodb-find
                      entity-type
                      entity-filter
                      projection
                      qsort
                      0
                      0
                      collation)]
      {:status (stc/ok)
       :headers {(eh/content-type) (mt/text-clojurescript)}
       :body {:status "success"
              :language @language
              :data db-result}})
   ))

(defn get-label
  "Gets label by label code and language"
  [label-code
   & [label-language]]
  (when (and label-code
             (number?
               label-code))
    (let [entity-type language-cname
          entity-filter {:code label-code}
          label-language (or label-language
                             :english)
          label-language (keyword
                           label-language)
          projection {:_id 0
                      label-language 1}
          db-result (mon/mongodb-find-one
                      entity-type
                      entity-filter
                      projection)]
      (or (get
            db-result
            label-language)
          (str
            label-code))
     ))
 )

(defn set-language
  "Set default language for logged in user"
  [request]
  (try
    (let [request-body (:body
                         request)
          language (or (:language request-body)
                       "english")
          language-name (or (:language-name request-body)
                            "English")
          session-obj (ssn/get-session-obj
                        request)
          preferences (ssn/get-preferences
                        request)]
      (if (and preferences
               (map?
                 preferences)
               (not
                 (empty?
                   preferences))
           )
        (mon/mongodb-update-by-id
          preferences-cname
          (:_id preferences)
          {:language language
           :language-name language-name})
        (mon/mongodb-insert-one
          preferences-cname
          {:user-id (:user-id session-obj)
           :language language
           :language-name language-name}))
      {:status (stc/ok)
       :headers {(eh/content-type) (mt/text-clojurescript)}
       :body {:status "success"}})
    (catch Exception e
      (println
        (.getMessage
          e))
      {:status (stc/internal-server-error)
       :headers {(eh/content-type) (mt/text-clojurescript)}
       :body {:status "Error"
              :message (.getMessage
                         e)}})
   ))

