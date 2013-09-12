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
      (s! :line-height lh)
      (update-permalink-box))))

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
  (let [params (-> (js/decodeURIComponent s)
                   (str/split #"\?")
                   (last)
                   (str/split #"\&"))]
    (apply conj
           (map (fn [x]
                  (let [kv (str/split x #"=")]
                    {(keyword (first kv)) (last kv)}))
                params))))

(defn make-query-string
  "{:a \"b\" :c \"d\"} 맵에서 a=b&c=d query string을 만든다"
  [m]
  (letfn [(make-field-value-pair [elem]
            (str (name (first elem)) "=" (last elem)))]
    (reduce (fn [a b] (str a "&" b))
            (map make-field-value-pair m))))

(defn make-url [path query-string]
  (str (first (str/split path #"\?"))
       (when (not (nil? query-string))
         (str "?" query-string))))

(defn make-permalink []
  (make-url js/location
            (make-query-string (style))))

(defn update-permalink-box []
  (dommy/set-value! (sel1 :#permalink)
                    (make-permalink)))

(defn make-tweet-url []
  (dommy/value (sel1 :#permalink)))

(defn applyqs [s]
  (do
    (set-style! (parse-query-string s))
    (sync-style-input-box)))

(set! (.-onload js/window)
      (fn []
        (applyqs js/location)))
