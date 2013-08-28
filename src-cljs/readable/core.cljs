(ns readable.core
  (:use-macros [dommy.macros :only [sel1]])
  (:require [dommy.utils :as utils]
            [dommy.core :as dommy]))

(dommy/listen! (sel1 :#font-family)
               :change
               (fn []
                 (dommy/set-style!
                  (sel1 ".content")
                  :font-family
                  (dommy/value (sel1 :#font-family)))))

(dommy/listen! (sel1 :#font-size)
               :change
               (fn []
                 (dommy/set-style!
                  (sel1 ".content")
                  :font-size
                  (dommy/value (sel1 :#font-size)))))

(dommy/listen! (sel1 :#line-height)
               :change
               (fn []
                 (dommy/set-style!
                  (sel1 ".content")
                  :line-height
                  (dommy/value (sel1 :#line-height)))))
