(ns language-lib.core
  (:require [ajax-lib.core :refer [sjax get-response]]
            [common-middle.request-urls :as rurls]))

(def default-language
     (atom "english"))

(def cached-labels
     (atom []))

(defn read-all-labels
  "Read all labels if cached-labels is empty"
  []
  (if (empty?
        @cached-labels)
    (let [xhr (sjax
                {:url rurls/get-labels-url})
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
  "Get label for chosen language by it's code recursion"
  [code
   language
   parameters
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
                                      (keyword
                                        language))]
          (if parameters
            (if (vector?
                  parameters)
             (let [replaced-parameters (atom cached-translation)]
               (doseq [index (range
                               (count
                                 parameters))]
                 (swap!
                   replaced-parameters
                   (fn [a-val
                        param-value]
                     (.replace
                       a-val
                       (str
                         "$"
                         index)
                       param-value))
                   (get
                     parameters
                     index))
                )
               @replaced-parameters)
             (.replace
               cached-translation
               "$0"
               parameters))
            cached-translation)
          code)
        (recur
          code
          language
          parameters
          (inc
            index))
       ))
    code))

(defn get-label
  "Get label for chosen language by it's code"
  [code
   & [language
      parameters]]
  (read-all-labels)
  (get-label-recur
    code
    @default-language
    parameters
    0))

