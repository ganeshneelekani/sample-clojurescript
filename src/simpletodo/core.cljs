(ns simpletodo.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]))

;; -------------------------
;; Views

(def todos (r/atom [{:desc "Boil the pasta" :completed true}
                    {:desc "Grind the basil" :completed false}]))

(defn todo-item [desc]
  [:li {:style {:color (if (:completed desc)
                         "green"
                         "red")}}
   (:desc desc)])

(defn todo-form []
  (let [new-item (r/atom "")
        new-item-completed (r/atom false)]
    (fn []
      [:form {:on-submit (fn [e]
                           (.preventDefault e)
                           (swap! todos conj {:desc @new-item
                                              :completed @new-item-completed})
                           (reset! new-item "")
                           (reset! new-item-completed false))}
       [:input {:type "checkbox"
                :value @new-item-completed
                :on-change #(reset! new-item-completed (-> % .-target .-checked))}]
       [:input {:type "text"
                :placeholder " Add new Item "
                :value @new-item
                :on-change #(reset! new-item (-> % .-target .-value))}]])))

(defn todo-list []
  [:ul
   (for [todo @todos]
     (todo-item todo))])

(defn home-page []
  [:div [:h2 "Welcome to Reagent"]
   [:p "Add new item"]
   [todo-form]
   [todo-list]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
