(ns language-lib.core
  (:require [session-lib.core :as ssn]
            [mongo-lib.core :as mon]
            [dao-lib.core :as dao]
            [ajax-lib.http.entity-header :as eh]
            [ajax-lib.http.mime-type :as mt]
            [ajax-lib.http.status-code :as stc]))

(defn get-preferences
  "Fetch preferences for logged in user"
  [session-cookie
   session-type]
  (when-let [session (ssn/get-cookie
                       session-cookie
                       session-type)]
    (when-let [user-id (:user-id
                         (mon/mongodb-find-one
                           (name session-type)
                           {:uuid session}))]
      (when-let [preferences (mon/mongodb-find-one
                               "preferences"
                               {:user-id user-id})]
        preferences))
   ))

(defn get-labels
  "Read labels for chosen language"
  [request]
  (let [language (atom :english)]
    (when-let [session-cookie (:cookie request)]
      (when-let [preferences (get-preferences
                               session-cookie
                               :long-session)]
        (reset!
          language
          (keyword
            (:language preferences))
         ))
      (when-let [preferences (get-preferences
                               session-cookie
                               :session)]
        (reset!
          language
          (keyword
            (:language preferences))
         ))
     )
    (let [entity-type "language"
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
       :headers {(eh/content-type) (mt/text-plain)}
       :body (str {:status "success"
                   :language @language
                   :data db-result})})
   ))

(defn set-language
  "Set default language for logged in user"
  [request
   request-body]
  (let [language (name
                   (:language request-body))
        language-name (:language-name request-body)]
    (when-let [session-cookie (:cookie request)]
      (when-let [preferences (get-preferences
                               session-cookie
                               :long-session)]
        (mon/mongodb-update-by-id
          "preferences"
          (:_id preferences)
          {:language language
           :language-name language-name}))
      (when-let [preferences (get-preferences
                               session-cookie
                               :session)]
        (mon/mongodb-update-by-id
          "preferences"
          (:_id preferences)
          {:language language
           :language-name language-name}))
     )
    {:status (stc/ok)
     :headers {(eh/content-type) (mt/text-plain)}
     :body (str {:status "success"})})
 )

