import Braytech from '@Data/manifest/en-OwO/Braytech.json';
import BraytechActivityDifficulty from '@Data/manifest/en-OwO/BraytechActivityDifficulty.json';
import BraytechMaps from '@Data/manifest/en-OwO/BraytechMaps.json';
import BraytechMapsTypes from '@Data/manifest/en-OwO/BraytechMapsTypes.json';
import DestinyActivityDefinition from '@Data/manifest/en-OwO/DestinyActivityDefinition.json';
import DestinyActivityModeDefinition from '@Data/manifest/en-OwO/DestinyActivityModeDefinition.json';
import DestinyActivityModifierDefinition from '@Data/manifest/en-OwO/DestinyActivityModifierDefinition.json';
import DestinyActivityTypeDefinition from '@Data/manifest/en-OwO/DestinyActivityTypeDefinition.json';
import DestinyChecklistDefinition from '@Data/manifest/en-OwO/DestinyChecklistDefinition.json';
import DestinyCollectibleDefinition from '@Data/manifest/en-OwO/DestinyCollectibleDefinition.json';
import DestinyDamageTypeDefinition from '@Data/manifest/en-OwO/DestinyDamageTypeDefinition.json';
import DestinyDestinationDefinition from '@Data/manifest/en-OwO/DestinyDestinationDefinition.json';
import DestinyHistoricalStatsDefinition from '@Data/manifest/en-OwO/DestinyHistoricalStatsDefinition.json';
import DestinyInventoryBucketDefinition from '@Data/manifest/en-OwO/DestinyInventoryBucketDefinition.json';
import DestinyInventoryItemDefinition from '@Data/manifest/en-OwO/DestinyInventoryItemDefinition.json';
import DestinyMilestoneDefinition from '@Data/manifest/en-OwO/DestinyMilestoneDefinition.json';
import DestinyObjectiveDefinition from '@Data/manifest/en-OwO/DestinyObjectiveDefinition.json';
import DestinyPhaseDefinition from '@Data/manifest/en-OwO/DestinyPhaseDefinition.json';
import DestinyPresentationNodeDefinition from '@Data/manifest/en-OwO/DestinyPresentationNodeDefinition.json';
import DestinyRecordDefinition from '@Data/manifest/en-OwO/DestinyRecordDefinition.json';
import DestinySeasonDefinition from '@Data/manifest/en-OwO/DestinySeasonDefinition.json';
import DestinySocketCategoryDefinition from '@Data/manifest/en-OwO/DestinySocketCategoryDefinition.json';
import DestinySourceDefinition from '@Data/manifest/en-OwO/DestinySourceDefinition.json';
import DestinyStatDefinition from '@Data/manifest/en-OwO/DestinyStatDefinition.json';
import DestinyTraitDefinition from '@Data/manifest/en-OwO/DestinyTraitDefinition.json';

import DestinyInventoryItemDefinitionColloquial from '@Data/manifest/en-OwO/DestinyInventoryItemDefinitionColloquial.json';

const enOwO = {
  definitions: {
    Braytech,
    BraytechActivityDifficulty,
    BraytechMaps,
    BraytechMapsTypes,
    DestinyActivityDefinition,
    DestinyActivityModeDefinition,
    DestinyActivityModifierDefinition,
    DestinyActivityTypeDefinition,
    DestinyChecklistDefinition,
    DestinyCollectibleDefinition,
    DestinyDamageTypeDefinition,
    DestinyDestinationDefinition,
    DestinyHistoricalStatsDefinition,
    DestinyInventoryBucketDefinition,
    DestinyInventoryItemDefinition,
    DestinyMilestoneDefinition,
    DestinyObjectiveDefinition,
    DestinyPhaseDefinition,
    DestinyPresentationNodeDefinition,
    DestinyRecordDefinition,
    DestinySeasonDefinition,
    DestinySocketCategoryDefinition,
    DestinySourceDefinition,
    DestinyStatDefinition,
    DestinyTraitDefinition,
  },
  optional: {
    colloquialDefinitions: {
      DestinyInventoryItemDefinition: DestinyInventoryItemDefinitionColloquial,
    },
  },
};

export default enOwO;
