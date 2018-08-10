package com.melnick.java8;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Interfaces get some powerful new tools in Java 8.
 * The ability to add "default methods", as well as static methods have been added.
 *
 * The motivation behind the default method is twofold:
 * First, it provides an easy way to make API modifications to existing interfaces. Default
 * methods provide a means of adding functionality to an interface without needing to make changes
 * to implementations, or without risking breaking functionality.
 *
 * Second, it provides the ability to have a default implementation of a method when, for that
 * specific method, the interface is able to provide source.
 *
 * Previously, static methods were disallowed on interfaces. They are now allowed, though the
 * interface must provide the implementation, not the impl classes.
 *
 * A good example of retrofitting interfaces is in the List Interface, which now provides a default
 * implementation of sort(), rather than requiring Collections.sort(List e). ArrayList actually
 * overrides this default implementation and provides a better one for itself. Collections.sort()
 * now just calls the sort method on the list passed in, essentially making this call useless.
 *
 * Collections.sort(List list){
 *     list.sort(null)
 * }
 *
 * @see List#sort(Comparator)
 * @see Collections#sort(List)
 *
 */
public class DefaultAndStaticMethods {

    interface Meeting {
        LocalDate getMeetingDate();
    }







    interface Member {
        List<Meeting> getMeetingsAttended();
        void setAge();
        int getAge();
        void setName();
        String getName();
    }












    interface SecretSociety {

        void addMember(Member member);

        int getNumberOfMembers();

        List<Member> getMemberList();

        default double getAverageMemberAgeTraditional() {
            int totalAge = 0;
            for (Member member : getMemberList()) {
                totalAge += member.getAge();
            }
            return totalAge / getNumberOfMembers();
        }

        static double getAverageMemberAge(List<Member> memberList) {
            return memberList.stream()
                    .mapToDouble(Member::getAge)
                    .average()
                    .orElse(0);
        }

        default String getListOfNamesTraditional() {
            StringJoiner joiner = new StringJoiner(", ");  //NOTE: StringJoiner is a new class.
            for (Member member : getMemberList()) {
                joiner.add(member.getName());
            }
            return joiner.toString();
        }

        static String getListOfNames(List<Member> memberList) {
            return memberList.stream()
                    .map(Member::getName)
                    .collect(Collectors.joining(", "));
        }

        static List<LocalDate> getOrderedListOfMeetingDates(List<Member> memberList) {
            Set<LocalDate> meetingDates = new HashSet<>(); //to ensure no duplicates
            for (Member member : memberList) {
                for (Meeting meeting : member.getMeetingsAttended()) {
                    meetingDates.add(meeting.getMeetingDate());
                }
            }
            List<LocalDate> orderedMeetingDates = new ArrayList<>(meetingDates);
            Collections.sort(orderedMeetingDates);
            return orderedMeetingDates;
        }

        default List<LocalDate> getOrderedListOfMeetingDates() {
            return getMemberList().stream()
                    .map(Member::getMeetingsAttended)
                    .flatMap(Collection::stream)
                    .map(Meeting::getMeetingDate)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
        }

    }





















    class SkullAndBones implements SecretSociety {
        List<Member> memberList;

        @Override public void addMember(Member member) {
            if (memberList == null) {
                memberList = new ArrayList<>();
            }
            memberList.add(member);
        }

        @Override public int getNumberOfMembers() {
            return memberList.size();
        }

        @Override public List<Member> getMemberList() {
            return memberList;
        }

        //Syntax to override a default method, but also use a default method.
        //We dont' want to show every meeting. Remove every other meeting. They are SECRET
        @Override public List<LocalDate> getOrderedListOfMeetingDates() {
            List<LocalDate> orderedListOfMeetingDates =
                    SecretSociety.super.getOrderedListOfMeetingDates();
            for(int i = 0; i < orderedListOfMeetingDates.size(); i++) {
                if(i%2==0)
                    orderedListOfMeetingDates.remove(i);
            }
            return orderedListOfMeetingDates;
        }


    }



}
