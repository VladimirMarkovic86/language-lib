(ns language-lib.core-test
  (:require [clojure.test :refer :all]
            [language-lib.core :refer :all]
            [mongo-lib.core :as mon]))

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
       :serbian "Српски превод" }]))

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
        (nil?
          result)
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
