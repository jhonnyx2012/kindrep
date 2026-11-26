package com.jhonnyb.kindrep.ui.animation

import androidx.compose.runtime.Immutable
import com.jhonnyb.kindrep.domain.model.ExercisePhase
import com.jhonnyb.kindrep.generated.resources.Res
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_setup
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_bird_dog_title
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_setup
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_bulgarian_split_squat_title
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_setup
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_cat_cow_title
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_setup
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_chair_sit_to_stand_title
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_setup
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_childs_pose_title
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_setup
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_chin_tuck_title
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_setup
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_clamshell_title
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_setup
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_dead_bug_title
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_setup
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_deadlift_hip_hinge_title
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_setup
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_glute_bridge_title
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_setup
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_hamstring_bridge_title
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_setup
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_heel_slides_title
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_setup
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_hip_flexor_stretch_title
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_setup
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_knee_to_chest_title
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_setup
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_lying_leg_curl_title
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_setup
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_mcgill_curl_up_title
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_setup
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_modified_side_plank_title
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_setup
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_pelvic_tilt_title
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_setup
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_piriformis_stretch_title
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_setup
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_prone_press_up_title
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_setup
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_seated_glute_release_title
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_setup
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_seated_knee_extension_title
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_setup
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_single_leg_hip_hinge_title
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_setup
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_soleus_heel_raise_title
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_setup
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_standing_calf_raise_title
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_setup
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_straight_leg_raise_title
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_setup
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_thoracic_open_book_title
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_setup
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_wall_calf_stretch_title
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_breathing
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_extend
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_hold
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_phase_return
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_setup
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_steady_cue
import com.jhonnyb.kindrep.generated.resources.exercise_wall_slide_title
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ExerciseCopyResources(
    val title: StringResource,
    val steadyFormCue: StringResource,
    val setupInstruction: StringResource,
    val breathingCue: StringResource,
    val phaseExtendLabel: StringResource,
    val phaseHoldLabel: StringResource,
    val phaseReturnLabel: StringResource,
) {
    fun phaseLabel(phase: ExercisePhase): StringResource = when (phase) {
        ExercisePhase.EXTEND -> phaseExtendLabel
        ExercisePhase.HOLD -> phaseHoldLabel
        ExercisePhase.RETURN -> phaseReturnLabel
    }
}

object ExerciseCopyCatalog {
    private val copyByMediaKey: Map<String, ExerciseCopyResources> = mapOf(
        ExerciseMediaKeys.CAT_COW to ExerciseCopyResources(
            title = Res.string.exercise_cat_cow_title,
            steadyFormCue = Res.string.exercise_cat_cow_steady_cue,
            setupInstruction = Res.string.exercise_cat_cow_setup,
            breathingCue = Res.string.exercise_cat_cow_breathing,
            phaseExtendLabel = Res.string.exercise_cat_cow_phase_extend,
            phaseHoldLabel = Res.string.exercise_cat_cow_phase_hold,
            phaseReturnLabel = Res.string.exercise_cat_cow_phase_return,
        ),
        ExerciseMediaKeys.BIRD_DOG to ExerciseCopyResources(
            title = Res.string.exercise_bird_dog_title,
            steadyFormCue = Res.string.exercise_bird_dog_steady_cue,
            setupInstruction = Res.string.exercise_bird_dog_setup,
            breathingCue = Res.string.exercise_bird_dog_breathing,
            phaseExtendLabel = Res.string.exercise_bird_dog_phase_extend,
            phaseHoldLabel = Res.string.exercise_bird_dog_phase_hold,
            phaseReturnLabel = Res.string.exercise_bird_dog_phase_return,
        ),
        ExerciseMediaKeys.GLUTE_BRIDGE to ExerciseCopyResources(
            title = Res.string.exercise_glute_bridge_title,
            steadyFormCue = Res.string.exercise_glute_bridge_steady_cue,
            setupInstruction = Res.string.exercise_glute_bridge_setup,
            breathingCue = Res.string.exercise_glute_bridge_breathing,
            phaseExtendLabel = Res.string.exercise_glute_bridge_phase_extend,
            phaseHoldLabel = Res.string.exercise_glute_bridge_phase_hold,
            phaseReturnLabel = Res.string.exercise_glute_bridge_phase_return,
        ),
        ExerciseMediaKeys.DEAD_BUG to ExerciseCopyResources(
            title = Res.string.exercise_dead_bug_title,
            steadyFormCue = Res.string.exercise_dead_bug_steady_cue,
            setupInstruction = Res.string.exercise_dead_bug_setup,
            breathingCue = Res.string.exercise_dead_bug_breathing,
            phaseExtendLabel = Res.string.exercise_dead_bug_phase_extend,
            phaseHoldLabel = Res.string.exercise_dead_bug_phase_hold,
            phaseReturnLabel = Res.string.exercise_dead_bug_phase_return,
        ),
        ExerciseMediaKeys.CHILDS_POSE to ExerciseCopyResources(
            title = Res.string.exercise_childs_pose_title,
            steadyFormCue = Res.string.exercise_childs_pose_steady_cue,
            setupInstruction = Res.string.exercise_childs_pose_setup,
            breathingCue = Res.string.exercise_childs_pose_breathing,
            phaseExtendLabel = Res.string.exercise_childs_pose_phase_extend,
            phaseHoldLabel = Res.string.exercise_childs_pose_phase_hold,
            phaseReturnLabel = Res.string.exercise_childs_pose_phase_return,
        ),
        ExerciseMediaKeys.CLAMSHELL to ExerciseCopyResources(
            title = Res.string.exercise_clamshell_title,
            steadyFormCue = Res.string.exercise_clamshell_steady_cue,
            setupInstruction = Res.string.exercise_clamshell_setup,
            breathingCue = Res.string.exercise_clamshell_breathing,
            phaseExtendLabel = Res.string.exercise_clamshell_phase_extend,
            phaseHoldLabel = Res.string.exercise_clamshell_phase_hold,
            phaseReturnLabel = Res.string.exercise_clamshell_phase_return,
        ),
        ExerciseMediaKeys.WALL_SLIDE to ExerciseCopyResources(
            title = Res.string.exercise_wall_slide_title,
            steadyFormCue = Res.string.exercise_wall_slide_steady_cue,
            setupInstruction = Res.string.exercise_wall_slide_setup,
            breathingCue = Res.string.exercise_wall_slide_breathing,
            phaseExtendLabel = Res.string.exercise_wall_slide_phase_extend,
            phaseHoldLabel = Res.string.exercise_wall_slide_phase_hold,
            phaseReturnLabel = Res.string.exercise_wall_slide_phase_return,
        ),
        ExerciseMediaKeys.CHIN_TUCK to ExerciseCopyResources(
            title = Res.string.exercise_chin_tuck_title,
            steadyFormCue = Res.string.exercise_chin_tuck_steady_cue,
            setupInstruction = Res.string.exercise_chin_tuck_setup,
            breathingCue = Res.string.exercise_chin_tuck_breathing,
            phaseExtendLabel = Res.string.exercise_chin_tuck_phase_extend,
            phaseHoldLabel = Res.string.exercise_chin_tuck_phase_hold,
            phaseReturnLabel = Res.string.exercise_chin_tuck_phase_return,
        ),
        ExerciseMediaKeys.BULGARIAN_SPLIT_SQUAT to ExerciseCopyResources(
            title = Res.string.exercise_bulgarian_split_squat_title,
            steadyFormCue = Res.string.exercise_bulgarian_split_squat_steady_cue,
            setupInstruction = Res.string.exercise_bulgarian_split_squat_setup,
            breathingCue = Res.string.exercise_bulgarian_split_squat_breathing,
            phaseExtendLabel = Res.string.exercise_bulgarian_split_squat_phase_extend,
            phaseHoldLabel = Res.string.exercise_bulgarian_split_squat_phase_hold,
            phaseReturnLabel = Res.string.exercise_bulgarian_split_squat_phase_return,
        ),
        ExerciseMediaKeys.KNEE_TO_CHEST to ExerciseCopyResources(
            title = Res.string.exercise_knee_to_chest_title,
            steadyFormCue = Res.string.exercise_knee_to_chest_steady_cue,
            setupInstruction = Res.string.exercise_knee_to_chest_setup,
            breathingCue = Res.string.exercise_knee_to_chest_breathing,
            phaseExtendLabel = Res.string.exercise_knee_to_chest_phase_extend,
            phaseHoldLabel = Res.string.exercise_knee_to_chest_phase_hold,
            phaseReturnLabel = Res.string.exercise_knee_to_chest_phase_return,
        ),
        ExerciseMediaKeys.HEEL_SLIDES to ExerciseCopyResources(
            title = Res.string.exercise_heel_slides_title,
            steadyFormCue = Res.string.exercise_heel_slides_steady_cue,
            setupInstruction = Res.string.exercise_heel_slides_setup,
            breathingCue = Res.string.exercise_heel_slides_breathing,
            phaseExtendLabel = Res.string.exercise_heel_slides_phase_extend,
            phaseHoldLabel = Res.string.exercise_heel_slides_phase_hold,
            phaseReturnLabel = Res.string.exercise_heel_slides_phase_return,
        ),
        ExerciseMediaKeys.STRAIGHT_LEG_RAISE to ExerciseCopyResources(
            title = Res.string.exercise_straight_leg_raise_title,
            steadyFormCue = Res.string.exercise_straight_leg_raise_steady_cue,
            setupInstruction = Res.string.exercise_straight_leg_raise_setup,
            breathingCue = Res.string.exercise_straight_leg_raise_breathing,
            phaseExtendLabel = Res.string.exercise_straight_leg_raise_phase_extend,
            phaseHoldLabel = Res.string.exercise_straight_leg_raise_phase_hold,
            phaseReturnLabel = Res.string.exercise_straight_leg_raise_phase_return,
        ),
        ExerciseMediaKeys.PRONE_PRESS_UP to ExerciseCopyResources(
            title = Res.string.exercise_prone_press_up_title,
            steadyFormCue = Res.string.exercise_prone_press_up_steady_cue,
            setupInstruction = Res.string.exercise_prone_press_up_setup,
            breathingCue = Res.string.exercise_prone_press_up_breathing,
            phaseExtendLabel = Res.string.exercise_prone_press_up_phase_extend,
            phaseHoldLabel = Res.string.exercise_prone_press_up_phase_hold,
            phaseReturnLabel = Res.string.exercise_prone_press_up_phase_return,
        ),
        ExerciseMediaKeys.HALF_KNEELING_HIP_FLEXOR to ExerciseCopyResources(
            title = Res.string.exercise_hip_flexor_stretch_title,
            steadyFormCue = Res.string.exercise_hip_flexor_stretch_steady_cue,
            setupInstruction = Res.string.exercise_hip_flexor_stretch_setup,
            breathingCue = Res.string.exercise_hip_flexor_stretch_breathing,
            phaseExtendLabel = Res.string.exercise_hip_flexor_stretch_phase_extend,
            phaseHoldLabel = Res.string.exercise_hip_flexor_stretch_phase_hold,
            phaseReturnLabel = Res.string.exercise_hip_flexor_stretch_phase_return,
        ),
        ExerciseMediaKeys.MODIFIED_SIDE_PLANK to ExerciseCopyResources(
            title = Res.string.exercise_modified_side_plank_title,
            steadyFormCue = Res.string.exercise_modified_side_plank_steady_cue,
            setupInstruction = Res.string.exercise_modified_side_plank_setup,
            breathingCue = Res.string.exercise_modified_side_plank_breathing,
            phaseExtendLabel = Res.string.exercise_modified_side_plank_phase_extend,
            phaseHoldLabel = Res.string.exercise_modified_side_plank_phase_hold,
            phaseReturnLabel = Res.string.exercise_modified_side_plank_phase_return,
        ),
        ExerciseMediaKeys.CHAIR_SIT_TO_STAND to ExerciseCopyResources(
            title = Res.string.exercise_chair_sit_to_stand_title,
            steadyFormCue = Res.string.exercise_chair_sit_to_stand_steady_cue,
            setupInstruction = Res.string.exercise_chair_sit_to_stand_setup,
            breathingCue = Res.string.exercise_chair_sit_to_stand_breathing,
            phaseExtendLabel = Res.string.exercise_chair_sit_to_stand_phase_extend,
            phaseHoldLabel = Res.string.exercise_chair_sit_to_stand_phase_hold,
            phaseReturnLabel = Res.string.exercise_chair_sit_to_stand_phase_return,
        ),
        ExerciseMediaKeys.SEATED_KNEE_EXTENSION to ExerciseCopyResources(
            title = Res.string.exercise_seated_knee_extension_title,
            steadyFormCue = Res.string.exercise_seated_knee_extension_steady_cue,
            setupInstruction = Res.string.exercise_seated_knee_extension_setup,
            breathingCue = Res.string.exercise_seated_knee_extension_breathing,
            phaseExtendLabel = Res.string.exercise_seated_knee_extension_phase_extend,
            phaseHoldLabel = Res.string.exercise_seated_knee_extension_phase_hold,
            phaseReturnLabel = Res.string.exercise_seated_knee_extension_phase_return,
        ),
        ExerciseMediaKeys.STANDING_CALF_RAISE to ExerciseCopyResources(
            title = Res.string.exercise_standing_calf_raise_title,
            steadyFormCue = Res.string.exercise_standing_calf_raise_steady_cue,
            setupInstruction = Res.string.exercise_standing_calf_raise_setup,
            breathingCue = Res.string.exercise_standing_calf_raise_breathing,
            phaseExtendLabel = Res.string.exercise_standing_calf_raise_phase_extend,
            phaseHoldLabel = Res.string.exercise_standing_calf_raise_phase_hold,
            phaseReturnLabel = Res.string.exercise_standing_calf_raise_phase_return,
        ),
        ExerciseMediaKeys.THORACIC_OPEN_BOOK to ExerciseCopyResources(
            title = Res.string.exercise_thoracic_open_book_title,
            steadyFormCue = Res.string.exercise_thoracic_open_book_steady_cue,
            setupInstruction = Res.string.exercise_thoracic_open_book_setup,
            breathingCue = Res.string.exercise_thoracic_open_book_breathing,
            phaseExtendLabel = Res.string.exercise_thoracic_open_book_phase_extend,
            phaseHoldLabel = Res.string.exercise_thoracic_open_book_phase_hold,
            phaseReturnLabel = Res.string.exercise_thoracic_open_book_phase_return,
        ),
        ExerciseMediaKeys.PELVIC_TILT to ExerciseCopyResources(
            title = Res.string.exercise_pelvic_tilt_title,
            steadyFormCue = Res.string.exercise_pelvic_tilt_steady_cue,
            setupInstruction = Res.string.exercise_pelvic_tilt_setup,
            breathingCue = Res.string.exercise_pelvic_tilt_breathing,
            phaseExtendLabel = Res.string.exercise_pelvic_tilt_phase_extend,
            phaseHoldLabel = Res.string.exercise_pelvic_tilt_phase_hold,
            phaseReturnLabel = Res.string.exercise_pelvic_tilt_phase_return,
        ),
        ExerciseMediaKeys.MCGILL_CURL_UP to ExerciseCopyResources(
            title = Res.string.exercise_mcgill_curl_up_title,
            steadyFormCue = Res.string.exercise_mcgill_curl_up_steady_cue,
            setupInstruction = Res.string.exercise_mcgill_curl_up_setup,
            breathingCue = Res.string.exercise_mcgill_curl_up_breathing,
            phaseExtendLabel = Res.string.exercise_mcgill_curl_up_phase_extend,
            phaseHoldLabel = Res.string.exercise_mcgill_curl_up_phase_hold,
            phaseReturnLabel = Res.string.exercise_mcgill_curl_up_phase_return,
        ),
        ExerciseMediaKeys.HAMSTRING_BRIDGE to ExerciseCopyResources(
            title = Res.string.exercise_hamstring_bridge_title,
            steadyFormCue = Res.string.exercise_hamstring_bridge_steady_cue,
            setupInstruction = Res.string.exercise_hamstring_bridge_setup,
            breathingCue = Res.string.exercise_hamstring_bridge_breathing,
            phaseExtendLabel = Res.string.exercise_hamstring_bridge_phase_extend,
            phaseHoldLabel = Res.string.exercise_hamstring_bridge_phase_hold,
            phaseReturnLabel = Res.string.exercise_hamstring_bridge_phase_return,
        ),
        ExerciseMediaKeys.LYING_LEG_CURL to ExerciseCopyResources(
            title = Res.string.exercise_lying_leg_curl_title,
            steadyFormCue = Res.string.exercise_lying_leg_curl_steady_cue,
            setupInstruction = Res.string.exercise_lying_leg_curl_setup,
            breathingCue = Res.string.exercise_lying_leg_curl_breathing,
            phaseExtendLabel = Res.string.exercise_lying_leg_curl_phase_extend,
            phaseHoldLabel = Res.string.exercise_lying_leg_curl_phase_hold,
            phaseReturnLabel = Res.string.exercise_lying_leg_curl_phase_return,
        ),
        ExerciseMediaKeys.SINGLE_LEG_HIP_HINGE to ExerciseCopyResources(
            title = Res.string.exercise_single_leg_hip_hinge_title,
            steadyFormCue = Res.string.exercise_single_leg_hip_hinge_steady_cue,
            setupInstruction = Res.string.exercise_single_leg_hip_hinge_setup,
            breathingCue = Res.string.exercise_single_leg_hip_hinge_breathing,
            phaseExtendLabel = Res.string.exercise_single_leg_hip_hinge_phase_extend,
            phaseHoldLabel = Res.string.exercise_single_leg_hip_hinge_phase_hold,
            phaseReturnLabel = Res.string.exercise_single_leg_hip_hinge_phase_return,
        ),
        ExerciseMediaKeys.SOLEUS_HEEL_RAISE to ExerciseCopyResources(
            title = Res.string.exercise_soleus_heel_raise_title,
            steadyFormCue = Res.string.exercise_soleus_heel_raise_steady_cue,
            setupInstruction = Res.string.exercise_soleus_heel_raise_setup,
            breathingCue = Res.string.exercise_soleus_heel_raise_breathing,
            phaseExtendLabel = Res.string.exercise_soleus_heel_raise_phase_extend,
            phaseHoldLabel = Res.string.exercise_soleus_heel_raise_phase_hold,
            phaseReturnLabel = Res.string.exercise_soleus_heel_raise_phase_return,
        ),
        ExerciseMediaKeys.WALL_CALF_STRETCH to ExerciseCopyResources(
            title = Res.string.exercise_wall_calf_stretch_title,
            steadyFormCue = Res.string.exercise_wall_calf_stretch_steady_cue,
            setupInstruction = Res.string.exercise_wall_calf_stretch_setup,
            breathingCue = Res.string.exercise_wall_calf_stretch_breathing,
            phaseExtendLabel = Res.string.exercise_wall_calf_stretch_phase_extend,
            phaseHoldLabel = Res.string.exercise_wall_calf_stretch_phase_hold,
            phaseReturnLabel = Res.string.exercise_wall_calf_stretch_phase_return,
        ),
        ExerciseMediaKeys.DEADLIFT_HIP_HINGE to ExerciseCopyResources(
            title = Res.string.exercise_deadlift_hip_hinge_title,
            steadyFormCue = Res.string.exercise_deadlift_hip_hinge_steady_cue,
            setupInstruction = Res.string.exercise_deadlift_hip_hinge_setup,
            breathingCue = Res.string.exercise_deadlift_hip_hinge_breathing,
            phaseExtendLabel = Res.string.exercise_deadlift_hip_hinge_phase_extend,
            phaseHoldLabel = Res.string.exercise_deadlift_hip_hinge_phase_hold,
            phaseReturnLabel = Res.string.exercise_deadlift_hip_hinge_phase_return,
        ),
        ExerciseMediaKeys.PIRIFORMIS_STRETCH to ExerciseCopyResources(
            title = Res.string.exercise_piriformis_stretch_title,
            steadyFormCue = Res.string.exercise_piriformis_stretch_steady_cue,
            setupInstruction = Res.string.exercise_piriformis_stretch_setup,
            breathingCue = Res.string.exercise_piriformis_stretch_breathing,
            phaseExtendLabel = Res.string.exercise_piriformis_stretch_phase_extend,
            phaseHoldLabel = Res.string.exercise_piriformis_stretch_phase_hold,
            phaseReturnLabel = Res.string.exercise_piriformis_stretch_phase_return,
        ),
        ExerciseMediaKeys.SEATED_GLUTE_RELEASE to ExerciseCopyResources(
            title = Res.string.exercise_seated_glute_release_title,
            steadyFormCue = Res.string.exercise_seated_glute_release_steady_cue,
            setupInstruction = Res.string.exercise_seated_glute_release_setup,
            breathingCue = Res.string.exercise_seated_glute_release_breathing,
            phaseExtendLabel = Res.string.exercise_seated_glute_release_phase_extend,
            phaseHoldLabel = Res.string.exercise_seated_glute_release_phase_hold,
            phaseReturnLabel = Res.string.exercise_seated_glute_release_phase_return,
        ),
    )

    fun resolve(mediaKey: String): ExerciseCopyResources =
        copyByMediaKey[mediaKey] ?: copyByMediaKey.getValue(ExerciseMediaKeys.BIRD_DOG)
}
