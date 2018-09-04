(ns language-lib.core
  (:require [ajax-lib.core :refer [sjax get-response]]))

(def default-language
     (atom :english))

(def cached-labels
     (atom []))

(defn read-all-labels
  ""
  []
  (if (empty? @cached-labels)
    (let [xhr (sjax
                {:url "/clojure/get-labels"})
          response (get-response
                     xhr)
          language (:language response)
          data (:data response)]
      (reset!
        default-language
        language)
      (reset!
        cached-labels
        data))
    @cached-labels))

(defn get-label-recur
  ""
  [code
   language
   index]
  (if (< index
         (count
           @cached-labels))
    (let [cached-label (get
                         @cached-labels
                         index)]
      (if (= (:code cached-label)
             code)
        (if-let [cached-translation (get
                                      cached-label
                                      language)]
          cached-translation
          code)
        (recur
          code
          language
          (inc index))
       ))
    code))

(defn get-label
  ""
  [code
   & [language]]
  (read-all-labels)
  (get-label-recur
    code
    @default-language
    0))

