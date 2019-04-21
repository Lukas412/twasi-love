package net.twasiplugin.lukasweber.commands;

public enum LoveAnswers {

    CLOUD {
        @Override
        public String toString() {
            return "love.answers.cloud";
        }
    },

    PERCENT {
        @Override
        public String toString() {
            return "love.answers.percent";
        }
    },

    LEVEL {
        @Override
        public String toString() {
            return "love.answers.level";
        }
    },

    MATH {
        @Override
        public String toString() {
            return "love.answers.math";
        }
    }

}
