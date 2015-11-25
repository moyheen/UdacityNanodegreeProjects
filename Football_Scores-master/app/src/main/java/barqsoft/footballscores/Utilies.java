package barqsoft.footballscores;

import android.content.Context;

import static barqsoft.footballscores.R.*;
import static barqsoft.footballscores.R.string.*;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies {
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static String getLeague(int league_num, Context context) {
        switch (league_num) {
            case SERIE_A:
                return context.getResources().getString(seriaa);
            case PREMIER_LEGAUE:
                return context.getResources().getString(premierleague);
            case CHAMPIONS_LEAGUE:
                return context.getResources().getString(champions_league);
            case PRIMERA_DIVISION:
                return context.getResources().getString(primeradivison);
            case BUNDESLIGA:
                return context.getResources().getString(bundesliga);
            default:
                return context.getResources().getString(not_known_league);
        }
    }

    public static String getMatchDay(int match_day, int league_num, Context context) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getResources().getString(group_stage_match_day);
            } else if (match_day == 7 || match_day == 8) {
                return context.getResources().getString(first_knockout_round);
            } else if (match_day == 9 || match_day == 10) {
                return context.getResources().getString(quarter_final);
            } else if (match_day == 11 || match_day == 12) {
                return context.getResources().getString(semi_final);
            } else {
                return context.getResources().getString(final_text);
            }
        } else {
            return context.getResources().getString(second_matchday_text) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamname, Context context) {
        if (teamname == null) {
            return drawable.no_icon;
        }
        if (teamname.equals(context.getResources().getString(string.arsenal))) {
            return drawable.arsenal;
        } else if (teamname.equals(context.getResources().getString(string.manchester))) {
            return drawable.manchester_united;
        } else if (teamname.equals(context.getResources().getString(string.swansea))) {
            return drawable.swansea_city_afc;
        } else if (teamname.equals(context.getResources().getString(string.leicester))) {
            return drawable.leicester_city_fc_hd_logo;
        } else if (teamname.equals(context.getResources().getString(string.everton))) {
            return drawable.everton_fc_logo1;
        } else if (teamname.equals(context.getResources().getString(string.westham))) {
            return drawable.west_ham;
        } else if (teamname.equals(context.getResources().getString(string.tottenham))) {
            return drawable.tottenham_hotspur;
        } else if (teamname.equals(context.getResources().getString(string.westbromich))) {
            return drawable.west_bromwich_albion_hd_logo;
        } else if (teamname.equals(context.getResources().getString(string.sunderland))) {
            return drawable.sunderland;
        } else if (teamname.equals(context.getResources().getString(string.stokecity))) {
            return drawable.stoke_city;
        } else {
            return drawable.no_icon;
        }
    }
}
