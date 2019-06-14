(ns language-lib.core-test
  (:require [clojure.test :refer :all]
            [language-lib.core :refer :all]
            [mongo-lib.core :as mon]
            [session-lib.core :as ssn]))

(def db-uri
     (or (System/getenv "MONGODB_URI")
         (System/getenv "PROD_MONGODB")
         "mongodb://admin:passw0rd@127.0.0.1:27017/admin"))

(def db-name
     "test-db")

(defn create-db
  "Create database for testing"
  []
  (mon/mongodb-connect
    db-uri
    db-name)
  (mon/mongodb-insert-many
    "language"
    [{ :code 1
       :english "English translation"
       :serbian "Српски превод" }
     { :code 2
       :english "English translation"
       :serbian "Српски превод" }])
  (mon/mongodb-insert-many
    "user"
    [{ :username "test-user"
	      :email "234@234" }])
	 (let [user-db-obj (mon/mongodb-find-one
	                     "user"
	                     {:username "test-user"})
	       _id (:_id user-db-obj)]
	   (mon/mongodb-insert-many
      "session"
      [{:uuid "test-uuid"
        :user-agent "Test browser"
        :user-id _id
        :username "admin"
        :created-at (java.util.Date.)}
       ])
    (mon/mongodb-insert-many
      "preferences"
      [{:user-id _id
        :language "english"
        :language-name "English"}
       ])
   ))

(defn destroy-db
  "Destroy testing database"
  []
  (mon/mongodb-drop-database
    db-name)
  (mon/mongodb-disconnect))

(defn before-and-after-tests
  "Before and after tests"
  [f]
  (create-db)
  (f)
  (destroy-db))

(use-fixtures :each before-and-after-tests)

(deftest test-get-labels
  (testing "Test get labels"
    
    (let [request nil
          result (get-labels
                   request)
          [{element-1-code :code
            element-1-english :english}
           {element-2-code :code
            element-2-english :english}] (get-in
                                           result
                                           [:body
                                            :data])
          result (update-in
                   result
                   [:body]
                   dissoc
                   :data)]
      
      (is
        (= result
           {:status 200
            :headers {"Content-Type" "text/clojurescript"}
            :body {:status "success"
                   :language "english"}})
       )
      
      (is
        (= element-1-code
           1.0)
       )
      
      (is
        (= element-1-english
           "English translation")
       )
      
      (is
        (= element-2-code
           2.0)
       )
      
      (is
        (= element-2-english
           "English translation")
       )
      
      
     )
    
   ))

(deftest test-get-label
  (testing "Test get label"
    
    (let [label-code nil
          label-language nil
          result (get-label
                   label-code
                   label-language)]
      
      (is
        (nil?
          result)
       )
      
     )
    
    (let [label-code 0
          label-language nil
          result (get-label
                   label-code
                   label-language)]
      
      (is
        (= result
           "0")
       )
      
     )
    
    (let [label-code 1
          label-language nil
          result (get-label
                   label-code
                   label-language)]
      
      (is
        (not
          (nil?
            result))
       )
      
      (is
        (string?
          result)
       )
      
      (is
        (= result
           "English translation")
       )
      
     )
    
    (let [label-code 2
          label-language "serbian"
          result (get-label
                   label-code
                   label-language)]
      
      (is
        (not
          (nil?
            result))
       )
      
      (is
        (string?
          result)
       )
      
      (is
        (= result
           "Српски превод")
       )
      
     )
    
    (let [label-code 2
          label-language :serbian
          result (get-label
                   label-code
                   label-language)]
      
      (is
        (not
          (nil?
            result))
       )
      
      (is
        (string?
          result)
       )
      
      (is
        (= result
           "Српски превод")
       )
      
     )
    
   ))

(deftest test-set-language
  (testing "Test set language"
    
    (let [request nil
          result (set-language
                   request)]
      
      (is
        (= result
           {:status 500
            :headers {"Content-Type" "text/clojurescript"}
            :body {:status "Error"
                   :message "The value for key user-id can not be null"}})
       )
      
     )
    
    (let [request {:cookie "session=test-uuid"}
          result (set-language
                   request)
          preferences-result (ssn/get-preferences
                               request)]
      
      (is
        (= result
           {:status 200
            :headers {"Content-Type" "text/clojurescript"}
            :body {:status "success"}})
       )
      
      (is
        (= (:language preferences-result)
           "english")
       )
      
      (is
        (= (:language-name preferences-result)
           "English")
       )
      
     )
    
    (let [request {:cookie "session=test-uuid"
                   :body {:language "serbian"
                          :language-name "Serbian"}}
          result (set-language
                   request)
          preferences-result (ssn/get-preferences
                               request)]
      
      (is
        (= result
           {:status 200
            :headers {"Content-Type" "text/clojurescript"}
            :body {:status "success"}})
       )
      
      (is
        (= (:language preferences-result)
           "serbian")
       )
      
      (is
        (= (:language-name preferences-result)
           "Serbian")
       )
      
     )
    
   ))

