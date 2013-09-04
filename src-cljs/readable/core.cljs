(ns readable.core
  (:use-macros [dommy.macros :only [sel1]])
  (:require [dommy.utils :as utils]
            [dommy.core :as dommy]
            [clojure.string :as str]))

(def content-elem (sel1 ".content"))

(defn set-style! [{ff :ff fs :fs lh :lh}]
  (letfn [(s! [t v]
            (when (not (nil? v))
              (dommy/set-style! content-elem t v)))]
    (do
      (s! :font-family ff)
      (s! :font-size fs)
      (s! :line-height lh))))

(defn style []
  (letfn [(s [k] (dommy/style content-elem k))]
    {:ff (s :font-family)
     :fs (s :font-size)
     :lh (s :line-height)}))

(dommy/listen! (sel1 :#font-family)
               :change
               (fn []
                 (set-style! {:ff (dommy/value (sel1 :#font-family))})))

(dommy/listen! (sel1 :#font-size)
               :change
               (fn []
                 (set-style! {:fs (dommy/value (sel1 :#font-size))})))

(dommy/listen! (sel1 :#line-height)
               :change
               (fn []
                 (set-style! {:lh (dommy/value (sel1 :#line-height))})))

(defn sync-style-input-box []
  (let [s (style)
        set-input-box! (fn [k v]
                         (dommy/set-value! (sel1 k) v))]
    (do
      (set-input-box! :#font-family (s :ff))
      (set-input-box! :#font-size (s :fs))
      (set-input-box! :#line-height (s :lh)))))

(defn parse-query-string
  "path?a=b&c=d 에서 query string을 뽑아 {:a \"b\" :c \"d\"} 맵을 만든다"
  [s]
  (let [params (-> (str/split s #"\?")
                   (last)
                   (str/split #"\&"))]
    (apply conj
           (map (fn [x]
                  (let [kv (str/split x #"=")]
                    {(keyword (first kv)) (last kv)}))
                params))))

(defn sync-query-string-style [s]
  (set-style! (parse-query-string s)))

(set! (.-onload js/window)
      (fn []
        (do
          (sync-query-string-style js/location)
          (sync-style-input-box))))
