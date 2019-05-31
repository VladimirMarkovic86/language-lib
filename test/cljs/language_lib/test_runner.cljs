(ns language-lib.test-runner
  (:require [language-lib.core-test-cljs]
            [doo.runner :refer-macros [doo-tests doo-all-tests]]))

(enable-console-print!)

(doo-tests
  'language-lib.core-test-cljs)

