(ns readable.core
  (:require [clojure.browser.event :as event]
            [clojure.browser.dom :as dom]))

; 이벤트 이름 참고: 
; http://docs.closure-library.googlecode.com/git/closure_goog_events_eventtype.js.source.html
(event/listen (dom/get-element "font-size")
              :change
              (fn []
                (js/alert "test")))
