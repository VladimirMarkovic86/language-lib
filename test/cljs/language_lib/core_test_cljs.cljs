(ns language-lib.core-test-cljs
  (:require [clojure.test :refer-macros [deftest is testing]]
            [language-lib.core :refer [cached-labels read-all-labels get-label-recur
                                       get-label]]))

(reset!
  cached-labels
  [{:code 1.0
    :english "English"}
   {:code 2.0
    :english "Test"}
   {:code 3.0
    :english "Parameter name is $0, isn't it?"}
   {:code 4.0
    :english "Parameters names are $0 and $1, aren't they?"}
   ])

(deftest test-read-all-labels
  (testing
    
    (let [result (read-all-labels)]
      
      (is
        (= result
           @cached-labels)
       )
      
     )
    
   ))

(deftest test-get-label-recur
  (testing "Test get label recur"
    
    (let [code nil
          language nil
          parameters nil
          index nil
          result (get-label-recur
                   code
                   language
                   parameters
                   index)]
      
      (is
        (nil?
          result)
       )
      
     )
    
    (let [code 2.0
          language "english"
          parameters nil
          index 0
          result (get-label-recur
                   code
                   language
                   parameters
                   index)]
      
      (is
        (= result
           "Test")
       )
      
     )
    
    (let [code 3.0
          language "english"
          parameters "parameter-value"
          index 0
          result (get-label-recur
                   code
                   language
                   parameters
                   index)]
      
      (is
        (= result
           "Parameter name is parameter-value, isn't it?")
       )
      
     )
    
    (let [code 4.0
          language "english"
          parameters ["parameter-value-1"
                      "parameter-value-2"]
          index 0
          result (get-label-recur
                   code
                   language
                   parameters
                   index)]
      
      (is
        (= result
           "Parameters names are parameter-value-1 and parameter-value-2, aren't they?")
       )
      
     )
    
   ))

(deftest test-get-label
  (testing "Test get label"
    
    (let [code nil
          language nil
          parameters nil
          result (get-label
                   code
                   language
                   parameters)]
      
      (is
        (nil?
          result)
       )
      
     )
    
    (let [code 2.0
          language "english"
          parameters nil
          result (get-label
                   code
                   language
                   parameters)]
      
      (is
        (= result
           "Test")
       )
      
     )
    
    (let [code 3.0
          language "english"
          parameters "parameter-value"
          result (get-label
                   code
                   language
                   parameters)]
      
      (is
        (= result
           "Parameter name is parameter-value, isn't it?")
       )
      
     )
    
    (let [code 4.0
          language "english"
          parameters ["parameter-value-1"
                      "parameter-value-2"]
          result (get-label
                   code
                   language
                   parameters)]
      
      (is
        (= result
           "Parameters names are parameter-value-1 and parameter-value-2, aren't they?")
       )
      
     )
    
   ))

